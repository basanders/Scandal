package waveforms;

public class WavetableCosine extends Wavetable {

	public WavetableCosine() {
		super(4096);
		fillTable();
	}
	
	public WavetableCosine(int tableSize) {
		super(tableSize);
		fillTable();
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = Math.cos(i * twoPi / tableSize);
		}
	}

	@Override
	public double getSample(double phase, double frequency) {
		return wavetable[(int) phase];
	}
	
}
