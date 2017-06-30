package framework.waveforms;

public class WavetableWhite extends Wavetable implements WavetableNoise {
	
	int runningIndex = 0;

	public WavetableWhite() {
		super(88200);
		fillTable();
	}
	
	public WavetableWhite(int size) {
		super(size);
		fillTable();
	}

	@Override
	public void fillTable() {
		for (int i = 0; i < tableSize; i++) {
			wavetable[i] = 2 * (float) Math.random() - 1;
		}
	}

	@Override
	public float getSample(float phase, float frequency) {
		return getSample();
	}

	public float getSample() {
		if (runningIndex >= tableSize) runningIndex -= tableSize;
		return wavetable[runningIndex++];
	}

}
