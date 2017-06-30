package framework.generators;

import framework.utilities.Settings;
import framework.waveforms.NaiveWaveform;

public class NaiveOscillator {

	private final NaiveWaveform waveform;

	public NaiveOscillator(NaiveWaveform waveform) {
		this.waveform = waveform;
	}

	public float[] get(int duration, float amplitude, float frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		float oscFreq = frequency * waveform.twoPi / Settings.samplingRate;
		float oscPhase = 0;
		float[] buffer = new float[samples];
		for (int i = 0; i < samples; i++) {
			buffer[i] = amplitude * waveform.getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= waveform.twoPi) oscPhase -= waveform.twoPi;
		}
		return buffer;
	}

}
