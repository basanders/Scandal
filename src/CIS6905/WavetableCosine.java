package CIS6905;

public class WavetableCosine extends Wavetable {

	WavetableCosine() {
		super(4096);
		fillTable();
	}
	
	WavetableCosine(int tableSize) {
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
	public double getSample(double phase) {
		return wavetable[(int) phase];
	}
	
}
