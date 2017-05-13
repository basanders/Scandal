package CIS6905;

public abstract class Wavetable extends Waveform {
	
	public final int tableSize;
	double[] wavetable;
	
	Wavetable(int size) {
		super(false);
		tableSize = size;
		wavetable = new double[tableSize];
	}

	public abstract void fillTable();
	
}
