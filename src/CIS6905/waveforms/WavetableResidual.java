package CIS6905.waveforms;

public final class WavetableResidual extends Wavetable {
	
	private static int ripples = 2;
	private static WavetableResidual sharedInstance;
	
	public static WavetableResidual getSharedInstance() {
		if (sharedInstance == null) sharedInstance = new WavetableResidual(4096);
		return sharedInstance;
	}
		
	private WavetableResidual(int size) {
		super(size);
		fillTable();
	}

	@Override
	public void fillTable() {
		int center = tableSize / 2 - 1;
		double maximum = 0;
		double minimum = 0;
		double[] step = new double[tableSize];
		double[] array = new double[tableSize];
		for (int i = 0; i < array.length; i++) {
			double index = i - center; // center on table
			index *= ripples * Math.PI / array.length;
			array[i] = Math.sin(index) / index; // compute sinc
			if (i == center) array[i] = 1; // resolve discontinuity
			if (i > 0) array[i] += array[i - 1]; // integrate
			if (array[i] >= maximum) maximum = array[i];
			if (array[i] <= minimum) minimum = array[i];
		}
		for (int i = 0; i < array.length; i++) {
			array[i] -= (maximum + minimum) / 2; // offset
			array[i] /= (maximum + minimum) / 2; // normalize
			step[i] = i <= center ? 1 : -1; // compute step
			array[i] += step[i]; // compute rising edge residual
		}
		wavetable = array;
	}

	@Override
	public double getSample(double index, double frequency) {
		return wavetable[(int) index];
	}

}
