package framework.generators;

public class StereoPanner {

	public double[] process(double[] buffer, double value) {
		if (Math.abs(value) > 1) value /= value; // sanity check
		double left = Math.cos(Math.PI * (value + 1) / 4);
		double right = Math.sin(Math.PI * (value + 1) / 4);
		int samples = buffer.length * 2;
		double[] interleavedBuffer = new double[samples];
		for (int i = 0, j = 0; i < samples; i += 2, j++) {
			interleavedBuffer[i] = left * buffer[j];
			interleavedBuffer[i + 1] = right * buffer[j];
		}
		return interleavedBuffer;
	}

	public double[] process(double[] buffer, double[] values) {
		int samples = buffer.length * 2;
		double[] interleavedBuffer = new double[samples];
		double left = 0;
		double right = 0;
		double valueIndex = 0;
		double valueIncrement = (double) values.length / buffer.length;
		for (int i = 0, j = 0; i < samples; i += 2, j++) {
			left = Math.cos(Math.PI * (values[(int) valueIndex] + 1) / 4);
			right = Math.sin(Math.PI * (values[(int) valueIndex] + 1) / 4);
			interleavedBuffer[i] = left * buffer[j];
			interleavedBuffer[i + 1] = right * buffer[j];
			valueIndex += valueIncrement;
		}
		return interleavedBuffer;
	}

}
