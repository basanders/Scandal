package framework.waveforms;

public class AdditiveTriangle extends AdditiveWavetable {

	public AdditiveTriangle() {
		super(4096, 10);
	}
	
	public AdditiveTriangle(int tableSize, int harmonicCount) {
		super(tableSize, harmonicCount);
	}

	@Override
	public void fillTable() {
		double radians = 0;
		double maximum = 0;
		for (int i = 0; i < tableSize; i++) {
			radians = i * twoPi / tableSize;
			for (int j = 0; j < harmonicCount; j++) {
				double oddIndex = 2 * j + 1;
				wavetable[i] += Math.cos(oddIndex * radians) / (oddIndex * oddIndex);
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
