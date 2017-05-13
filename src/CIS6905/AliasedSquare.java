package CIS6905;

public class AliasedSquare extends AliasedWavetable {
	
	AliasedSquare() {
		super(4096);
	}
	
	AliasedSquare(int tableSize) {
		super(tableSize);
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = i < tableSize / 2 ? 1 : -1;
		}
	}

	@Override
	public double getSample(double phase) {
		return wavetable[(int) phase];
	}

}
