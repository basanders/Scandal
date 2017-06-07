package generators;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import utilities.Settings;
import waveforms.ClassicSawtooth;

public class PolyphonicSynthesizer extends MidiKeyboardController implements Runnable, LineListener {

	private final WavetableOscillator oscillator = new WavetableOscillator(new ClassicSawtooth());
	private final SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(Settings.stereo);
	private final ArrayList<MidiNote> midiNotes = new ArrayList<MidiNote>();
	private double masterVolume = 0.5;
	private double leftLevel = 0.7;
	private double rightLevel = 0.7;
	private int attackSamples = 441; // 10 ms
	private int decaySamples = 2205;
	private double sustainLevel = 0.5;
	private int releaseSamples = 22050;

	private static enum ADSR { ATTACK, DECAY, SUSTAIN, RELEASE, OFF }

	private class MidiNote {

		final double frequency;
		double amplitude;
		double frequencyIncrement;
		double phase = 0;
		double[] buffer = new double[Settings.vectorSize];
		ADSR envelopeStage = ADSR.OFF;
		int envelopeSamples;
		double envelopeSlope;
		double envelopeLevel;

		MidiNote(int midiNoteNumber) {
			this.frequency = midiToFrequency(midiNoteNumber);
			this.frequencyIncrement = this.frequency * oscillator.frequencyScale;
		}

		void updateAmplitude(int velocity) {
			if (velocity == 0) {
				envelopeStage = ADSR.RELEASE;
				envelopeSamples = releaseSamples;
				envelopeSlope = envelopeLevel / releaseSamples;
			} else {
				amplitude = (double) velocity / 127;
				envelopeStage = ADSR.ATTACK;
				envelopeSamples = attackSamples;
				envelopeSlope = (1.0 - envelopeLevel) / attackSamples;
			}
		}

		double[] getVector() {
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = envelopeLevel * amplitude * oscillator.wavetable.getSample(phase, frequencyIncrement);
				phase += frequencyIncrement;
				if (phase >= oscillator.wavetable.tableSize) phase -= oscillator.wavetable.tableSize;
				switch (envelopeStage) {
				case ATTACK: {
					envelopeLevel += envelopeSlope;
					envelopeSamples -= 1;
					if (envelopeSamples == 0) {
						envelopeStage = ADSR.DECAY;
						envelopeSamples = decaySamples;
						envelopeSlope = (envelopeLevel - sustainLevel) / decaySamples;
					}
				} break;
				case DECAY: {
					envelopeLevel -= envelopeSlope;
					envelopeSamples -= 1;
					if (envelopeSamples == 0) envelopeStage = ADSR.SUSTAIN;
				} break;
				case RELEASE: {
					envelopeLevel -= envelopeSlope;
					envelopeSamples -= 1;
					if (envelopeSamples == 0) {
						envelopeStage = ADSR.OFF;
						phase = 0;
					}
				} break;
				default: break;
				}
			}
			return buffer;
		}

	}

	public PolyphonicSynthesizer(int controller) throws Exception {
		super(controller);
		sourceDataLine.addLineListener(this);
		for (int i = 0; i < 128; i++) midiNotes.add(new MidiNote(i));
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			sourceDataLine.open(Settings.stereo, Settings.vectorSize * Settings.bitDepth / 8);
			sourceDataLine.start();
			double[] doubles = new double[2 * Settings.vectorSize];
			double[] noteVector;
			ByteBuffer buffer = ByteBuffer.allocate(2 * Settings.vectorSize * Settings.bitDepth / 8);
			while (device.isOpen()) {
				Arrays.fill(doubles, 0);
				buffer.clear();
				for (MidiNote note : midiNotes) {
					if (note.envelopeStage != ADSR.OFF) {
						noteVector = note.getVector();
						for (int i = 0, j = 0; i < doubles.length; i += 2, j++) {
							doubles[i] += leftLevel * noteVector[j];
							doubles[i + 1] += rightLevel * noteVector[j];
						}
					}
				}
				for (int i = 0; i < doubles.length; i++) {
					buffer.putShort((short) (masterVolume * doubles[i] * Short.MAX_VALUE));
				}
				sourceDataLine.write(buffer.array(), 0, buffer.position());
			}
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(LineEvent event) {
		System.out.println("Polyphonic synthesizer event: " + event.getType());
	}

	@Override
	public void handleNoteOn(int note, int velocity, int channel) {
		midiNotes.get(note).updateAmplitude(velocity);
	}

	@Override
	public void handleNoteOff(int note, int velocity, int channel) {
		midiNotes.get(note).updateAmplitude(0);
	}

	@Override
	public void handleVolumeChange(int value, int channel) {
		this.masterVolume = (double) value / 127;
	}

	@Override
	public void handlePanoramaChange(int value, int channel) {
		double scaledValue = (2.0 * value / 127) - 1;
		leftLevel = Math.cos(Math.PI * (scaledValue + 1) / 4);
		rightLevel = Math.sin(Math.PI * (scaledValue + 1) / 4);
	}

	@Override
	public void handleModulationWheelChange(int value, int channel) {}

	@Override
	public void handlePitchBend(int lsb, int msb, int channel) {}

	@Override
	public void handleControlChange(int controller, int value, int channel) {}

	@Override
	public void handleProgramChange(int program, int channel) {}

	@Override
	public void handleAftertouch(int note, int pressure, int channel) {}

	@Override
	public void handleChannelAftertouch(int pressure, int channel) {}

}
