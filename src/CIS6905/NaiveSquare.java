package CIS6905;

public class NaiveSquare extends NaiveWaveform {

	@Override
	public double getSample(double phase) {
		return phase < Math.PI ? 1 : -1;
	}

}
