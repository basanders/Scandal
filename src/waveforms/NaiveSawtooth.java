package waveforms;

public class NaiveSawtooth extends NaiveWaveform {

	@Override
	public double getSample(double phase, double frequency) {
		return 2 * (1 - phase / twoPi) - 1;
	}

}
