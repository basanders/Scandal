package framework.waveforms;

public class AliasedSawtooth extends AliasedWavetable {

	public AliasedSawtooth() {
		super(4096);
	}
	
	public AliasedSawtooth(int size) {
		super(size);
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = 2 * (1 - (float) i / tableSize) - 1;
		}
	}

	@Override
	public float getSample(float phase, float frequency) {
		return wavetable[(int) phase];
	}

}
