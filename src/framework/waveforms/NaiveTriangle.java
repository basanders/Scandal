package framework.waveforms;

public class NaiveTriangle extends NaiveWaveform {

	@Override
	public float getSample(float phase, float frequency) {
		return 2 * (float) Math.abs(1 - phase / Math.PI) - 1;
	}

}
