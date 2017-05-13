package CIS6905;

public abstract class Waveform {
	
	public final double twoPi = 2 * Math.PI;
	public final boolean isNaive;
	
	Waveform(boolean isNaive) {
		this.isNaive = isNaive;
	}
	
	public abstract double getSample(double phase);

}
