package framework.waveforms;

public class NaiveSawtooth extends NaiveWaveform {

	@Override
	public float getSample(float phase, float frequency) {
		return 2 * (1 - phase / twoPi) - 1;
	}

}
