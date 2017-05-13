package CIS6905;

public class WavetableBlep extends Wavetable {
	
	private static int ripples;
	private static WavetableBlep sharedInstance;
	
	public static WavetableBlep getSharedInstance() {
		if (sharedInstance == null) sharedInstance = new WavetableBlep(4096, 2);
		return sharedInstance;
	}
		
	protected WavetableBlep(int size, int ripples) {
		super(size);
		WavetableBlep.ripples = ripples;
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
	public double getSample(double index) {
		return wavetable[(int) index];
	}

}
