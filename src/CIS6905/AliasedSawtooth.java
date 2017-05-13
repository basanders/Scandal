package CIS6905;

public class AliasedSawtooth extends AliasedWavetable {

	AliasedSawtooth() {
		super(4096);
	}
	
	AliasedSawtooth(int size) {
		super(size);
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = 2 * (1 - (double) i / tableSize) - 1;
		}
	}

	@Override
	public double getSample(double phase) {
		return wavetable[(int) phase];
	}

}
