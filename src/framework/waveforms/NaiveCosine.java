package framework.waveforms;

public class NaiveCosine extends NaiveWaveform {

	@Override
	public float getSample(float phase, float frequency) {
		return (float) Math.cos(phase);
	}

}
