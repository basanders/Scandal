package CIS6905.waveforms;

public class NaiveSquare extends NaiveWaveform {

	@Override
	public double getSample(double phase, double frequency) {
		return phase < Math.PI ? 1 : -1;
	}

}
