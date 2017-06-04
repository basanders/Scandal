package generators;

import utilities.Settings;
import waveforms.NaiveWaveform;

public class NaiveOscillator {

	private final NaiveWaveform waveform;

	public NaiveOscillator(NaiveWaveform waveform) {
		this.waveform = waveform;
	}

	public double[] get(int duration, double amplitude, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscFreq = frequency * waveform.twoPi / Settings.samplingRate;
		double oscPhase = 0;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			buffer[i] = amplitude * waveform.getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= waveform.twoPi) oscPhase -= waveform.twoPi;
		}
		return buffer;
	}

}
