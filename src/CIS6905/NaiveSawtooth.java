package CIS6905;

public class NaiveSawtooth extends NaiveWaveform {

	@Override
	public double getSample(double phase) {
		return 2 * (1 - phase / twoPi) - 1;
	}

}
