package framework.generators;

import framework.utilities.Settings;
import framework.waveforms.WavetableNoise;
import framework.waveforms.WavetableWhite;

public class NoiseGenerator {

	private final WavetableNoise wavetable;

	public NoiseGenerator() {
		this.wavetable = new WavetableWhite();
	}

	public NoiseGenerator(WavetableNoise wavetable) {
		this.wavetable = wavetable;
	}

	public float[] get(int duration, float amplitude) {
		int samples = duration * Settings.samplingRate / 1000;
		float[] buffer = new float[samples];
		for (int i = 0; i < samples; i++) {
			buffer[i] = amplitude * wavetable.getSample();
		}
		return buffer;
	}

	public float[] get(int duration, float[] envelope) {
		int samples = duration * Settings.samplingRate / 1000;
		float oscAmp = 0;
		float envelopeIndex = 0;
		float envelopeIncrement = (float) envelope.length / samples;
		float[] buffer = new float[samples];
		for (int i = 0; i < samples; i++) {
			oscAmp = envelope[(int) envelopeIndex];
			buffer[i] = oscAmp * wavetable.getSample();
			envelopeIndex += envelopeIncrement;
		}
		return buffer;
	}

}
