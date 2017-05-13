package CIS6905;

public interface ClassicWaveform {
	
	static final double[] residuals = WavetableBlep.getSharedInstance().wavetable;
	static final int tableCenter = residuals.length / 2 - 1;
	
	public abstract double getSample(double phase, double frequency);

}
