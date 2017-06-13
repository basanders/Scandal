package framework.waveforms;

public class NaiveTriangle extends NaiveWaveform {

	@Override
	public double getSample(double phase, double frequency) {
		return 2 * Math.abs(1 - phase / Math.PI) - 1;
	}

}
