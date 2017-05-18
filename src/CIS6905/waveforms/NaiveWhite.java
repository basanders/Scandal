package CIS6905.waveforms;

public class NaiveWhite extends NaiveWaveform {

	@Override
	public double getSample(double phase, double frequency) {
		return 2.0 * Math.random() - 1.0;
	}

}
