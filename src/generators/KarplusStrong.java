package generators;

import utilities.Settings;
import waveforms.WavetableWhite;

public class KarplusStrong extends PolyphonicSynthesizer {

	public KarplusStrong() throws Exception {
		super(1, new WavetableWhite());
		for (int i = 0; i < 128; i++) midiNotes.add(new KarplusStrongNote(i));
		attackSamples = decaySamples = 44;
		releaseSamples = 441;
	}

	class KarplusStrongNote extends PolyphonicSynthesizer.MidiNote {

		int index = 0;
		int newIndex;
		final double delaySamples = (double) Settings.samplingRate / frequency;
		double[] circularBuffer = new double[(int) delaySamples];
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

}
