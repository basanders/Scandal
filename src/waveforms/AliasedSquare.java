package waveforms;

public class AliasedSquare extends AliasedWavetable {
	
	public AliasedSquare() {
		super(4096);
	}
	
	public AliasedSquare(int tableSize) {
		super(tableSize);
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = i < tableSize / 2 ? 1 : -1;
		}
	}

	@Override
	public double getSample(double phase, double frequency) {
		return wavetable[(int) phase];
	}

}
