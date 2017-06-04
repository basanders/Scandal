package generators;

import utilities.Settings;
import waveforms.WavetableNoise;

public class NoiseGenerator {

	private final WavetableNoise wavetable;

	public NoiseGenerator(WavetableNoise wavetable) {
		this.wavetable = wavetable;
	}

	public double[] get(int duration, double amplitude) {
		int samples = duration * Settings.samplingRate / 1000;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			buffer[i] = amplitude * wavetable.getSample();
		}
		return buffer;
	}

	public double[] get(int duration, double[] envelope) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = 0;
		double envelopeIndex = 0;
		double envelopeIncrement = (double) envelope.length / samples;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			oscAmp = envelope[(int) envelopeIndex];
			buffer[i] = oscAmp * wavetable.getSample();
			envelopeIndex += envelopeIncrement;
		}
		return buffer;
	}

}
