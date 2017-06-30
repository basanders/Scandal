package framework.waveforms;

public class NaiveSquare extends NaiveWaveform {

	@Override
	public float getSample(float phase, float frequency) {
		return phase < Math.PI ? 1 : -1;
	}

}
