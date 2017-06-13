package framework.waveforms;

public class AdditiveSawtooth extends AdditiveWavetable {

	public AdditiveSawtooth() {
		super(4096, 10);
	}
	
	public AdditiveSawtooth(int tableSize, int harmonicCount) {
		super(tableSize, harmonicCount);
	}

	@Override
	public void fillTable() {
		double radians = 0;
		double maximum = 0;
		for (int i = 0; i < tableSize; i++) {
			radians = i * twoPi / tableSize;
			for (int j = 1; j <= harmonicCount; j++) {
				wavetable[i] += Math.sin(radians * j) / j;
			}
			if (wavetable[i] >= maximum) maximum = wavetable[i];
		}
		double inverseMaximum = 1 / maximum;
		for (int k = 0; k < tableSize; k++) wavetable[k] *= inverseMaximum; // normalize
	}

	@Override
	public double getSample(double phase, double frequency) {
		return wavetable[(int) phase];
	}

}
