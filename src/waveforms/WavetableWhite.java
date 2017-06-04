package waveforms;

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
			wavetable[i] = 2.0 * Math.random() - 1.0;
		}
	}

	@Override
	public double getSample(double phase, double frequency) {
		return getSample();
	}

	public double getSample() {
		if (runningIndex >= tableSize) runningIndex -= tableSize;
		return wavetable[runningIndex++];
	}

}
