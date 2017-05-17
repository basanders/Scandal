package CIS6905.waveforms;

public class WavetableNoise extends Wavetable implements NoiseGenerator {
	
	int runningIndex = 0;

	public WavetableNoise() {
		super(88200);
		fillTable();
	}
	
	public WavetableNoise(int size) {
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
	public double getSample(double phase) {
		// You don't want this.
		return wavetable[(int) phase];
	}

	@Override
	public double getSample() {
		if (runningIndex >= tableSize) runningIndex -= tableSize;
		return wavetable[runningIndex++];
	}

}
