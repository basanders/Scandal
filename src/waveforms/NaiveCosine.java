package waveforms;

public class NaiveCosine extends NaiveWaveform {

	@Override
	public double getSample(double phase, double frequency) {
		return Math.cos(phase);
	}

}
