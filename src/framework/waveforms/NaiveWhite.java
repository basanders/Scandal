package framework.waveforms;

public class NaiveWhite extends NaiveWaveform {

	@Override
	public float getSample(float phase, float frequency) {
		return 2 * (float) Math.random() - 1;
	}

}
