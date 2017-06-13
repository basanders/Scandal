package framework.generators;

import framework.effects.RingModulator;
import framework.utilities.Settings;
import framework.waveforms.WavetableCosine;
import framework.waveforms.WavetableWhite;

public class KarplusStrong extends PolyphonicSynthesizer {

	private final RingModulator tremolo = new RingModulator(new WavetableCosine());
	public double tremoloSpeed;

	public KarplusStrong(int controller) throws Exception {
		super(controller, new WavetableWhite());
		attackSamples = decaySamples = releaseSamples = 44;
		sustainLevel = 1.0;
	}

	@Override
	public void fillMidiNotesArray() {
		for (int i = 0; i < 128; i++) midiNotes.add(new KarplusStrongNote(i));
	}

	class KarplusStrongNote extends PolyphonicSynthesizer.MidiNote {

		int index = 0;
		int newIndex;
		final double delaySamples = Math.ceil((double) Settings.samplingRate / frequency);
		final double[] circularBuffer = new double[(int) delaySamples];
		double feedback = 0.99;

		KarplusStrongNote(int midiNoteNumber) {
			super(midiNoteNumber);
			reset();
		}

		void reset() {
			for (int i = 0; i < circularBuffer.length; i++) {
				circularBuffer[i] = baseWavetable.getSample(0, 0);
			}
		}

		@Override
		double[] get() {
			for (int i = 0; i < vector.length; i++) {
				vector[i] = circularBuffer[index] * envelopeLevel * amplitude;
				newIndex = (index + 1) % circularBuffer.length;
				circularBuffer[index] += circularBuffer[newIndex];
				circularBuffer[index] *= 0.5 * feedback;
				index = newIndex;
				updateEnvelope();
				if (envelopeStage == ADSR.OFF) reset();
			}
			return vector;
		}

	}

	@Override
	public void processMasterEffects() {
		mixVector = tremolo.processVector(mixVector, 0.99, tremoloSpeed);
	}

	@Override
	public void handleModulationWheelChange(int value, int channel) {
		tremoloSpeed = 0.05 * value;
	}

}
