package framework.waveforms;

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
		for (int i = 0; i < tableSize; i++)
			wavetable[i] = (float) Math.cos(i * twoPi / tableSize);
	}

	@Override
	public float getSample(float phase, float frequency) {
		return wavetable[(int) phase];
	}
	
}
