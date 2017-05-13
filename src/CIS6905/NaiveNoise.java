package CIS6905;

public class NaiveNoise extends NaiveWaveform {

	@Override
	public double getSample(double phase) {
		return 2.0 * Math.random() - 1.0;
	}

}
