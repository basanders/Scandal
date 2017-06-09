package generators;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import utilities.Settings;
import waveforms.Wavetable;

public class PolyphonicSynthesizer extends MidiKeyboardController implements RealTimePerformer {

	public final Wavetable baseWavetable;
	public final ArrayList<MidiNote> midiNotes = new ArrayList<MidiNote>();
	public double masterVolume = 0.5;
	public double masterLeft = Math.sqrt(2) / 2;
	public double masterRight = Math.sqrt(2) / 2;
	public int attackSamples = 44;
	public int decaySamples = 2205;
	public double sustainLevel = 0.5;
	public int releaseSamples = 22050;
	private double[] noteVector;
	public double[] mixVector = new double[2 * Settings.vectorSize];
	private final ByteBuffer buffer = ByteBuffer.allocate(2 * Settings.vectorSize * Settings.bitDepth / 8);

	public static enum ADSR { ATTACK, DECAY, SUSTAIN, RELEASE, OFF }

	public PolyphonicSynthesizer(int controller, Wavetable baseWavetable) throws Exception {
		super(controller);
		this.baseWavetable = baseWavetable;
		fillMidiNotesArray();
	}

	public void fillMidiNotesArray() {
		for (int i = 0; i < 128; i++) midiNotes.add(new MidiNote(i));
	}

	public class MidiNote {

		double amplitude;
		final double frequency;
		double phase = 0;
		double phaseIncrement;
		ADSR envelopeStage = ADSR.OFF;
		int envelopeSamples;
		double envelopeSlope;
		double envelopeLevel;
		final double[] vector = new double[Settings.vectorSize];

		MidiNote(int midiNoteNumber) {
			frequency = midiToFrequency(midiNoteNumber);
			phaseIncrement = frequency * (double) baseWavetable.tableSize / Settings.samplingRate;
		}

		void updateVelocity(int velocity) {
			if (velocity != 0) {
				amplitude = (double) velocity / 127;
				envelopeStage = ADSR.ATTACK;
				envelopeSamples = attackSamples;
				envelopeSlope = (1.0 - envelopeLevel) / attackSamples;
			} else {
				envelopeStage = ADSR.RELEASE;
				envelopeSamples = releaseSamples;
				envelopeSlope = envelopeLevel / releaseSamples;
			}
		}

		void updateEnvelope() {
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

		double[] get() {
			for (int i = 0; i < vector.length; i++) {
				vector[i] = baseWavetable.getSample(phase, phaseIncrement);
				vector[i] *= envelopeLevel * amplitude;
				phase += phaseIncrement;
				if (phase >= baseWavetable.tableSize) phase -= baseWavetable.tableSize;
				updateEnvelope();
			}
			return vector;
		}

	}

	@Override
	public AudioFlow start() {
		AudioFlow flow = new AudioFlow(this, Settings.stereo);
		new Thread(flow).start();
		return flow;
	}

	@Override
	public ByteBuffer getVector() {
		Arrays.fill(mixVector, 0);
		buffer.clear();
		for (MidiNote note : midiNotes) {
			if (note.envelopeStage != ADSR.OFF) {
				noteVector = note.get();
				for (int i = 0, j = 0; i < mixVector.length; i += 2, j++) {
					mixVector[i] += masterLeft * noteVector[j];
					mixVector[i + 1] += masterRight * noteVector[j];
				}
			}
		}
		processMasterEffects();
		for (int i = 0; i < mixVector.length; i++) {
			buffer.putShort((short) (masterVolume * mixVector[i] * Short.MAX_VALUE));
		}
		return buffer;
	}

	public void processMasterEffects() {}

	@Override
	public void handleNoteOn(int note, int velocity, int channel) {
		midiNotes.get(note).updateVelocity(velocity);
	}

	@Override
	public void handleNoteOff(int note, int velocity, int channel) {
		midiNotes.get(note).updateVelocity(0);
	}

	@Override
	public void handleVolumeChange(int value, int channel) {
		this.masterVolume = (double) value / 127;
	}

	@Override
	public void handlePanoramaChange(int value, int channel) {
		double scaledValue = (2.0 * value / 127) - 1;
		masterLeft = Math.cos(Math.PI * (scaledValue + 1) / 4);
		masterRight = Math.sin(Math.PI * (scaledValue + 1) / 4);
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
