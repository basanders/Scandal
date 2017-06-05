package utilities;

public class StereoPanner {
	
	// TODO implement variable parameters

	public double[] process(double[] buffer, double left, double right) {
		left /= left + right;
		right /= left + right;
		int samples = buffer.length * 2;
		double[] interleavedBuffer = new double[samples];
		for (int i = 0, j = 0; i < samples; i += 2, j++) {
			interleavedBuffer[i] = Math.sin(left * Math.PI / 2) * buffer[j];
			interleavedBuffer[i + 1] = Math.sin(right * Math.PI / 2) * buffer[j];
		}
		return interleavedBuffer;
	}

}
