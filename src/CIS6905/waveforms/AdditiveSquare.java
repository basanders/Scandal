package CIS6905.waveforms;

public class AdditiveSquare extends AdditiveWavetable {

	public AdditiveSquare() {
		super(4096, 10);
	}
	
	public AdditiveSquare(int tableSize, int harmonicCount) {
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
				wavetable[i] += Math.sin(oddIndex * radians) / oddIndex;
			}
			if (wavetable[i] >= maximum) maximum = wavetable[i];
		}
		double inverseMaximum = 1 / maximum;
		for (int k = 0; k < tableSize; k++) wavetable[k] *= inverseMaximum; // normalize
	}

	@Override
	public double getSample(double phase) {
		return wavetable[(int) phase];
	}

}
