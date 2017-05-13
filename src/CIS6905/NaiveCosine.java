package CIS6905;

public class NaiveCosine extends NaiveWaveform {

	@Override
	public double getSample(double phase) {
		return Math.cos(phase);
	}

}
