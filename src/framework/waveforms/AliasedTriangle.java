package framework.waveforms;

public class AliasedTriangle extends AliasedWavetable {
	
	// TODO Antialiased triangle generator

	public AliasedTriangle() {
		super(4096);
	}
	
	public AliasedTriangle(int tableSize) {
		super(tableSize);
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = 2 * Math.abs(1 - (double) i * 2 / tableSize) - 1;
		}
	}

	@Override
	public double getSample(double phase, double frequency) {
		return wavetable[(int) phase];
	}

}
