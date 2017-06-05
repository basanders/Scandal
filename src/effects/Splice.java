package effects;

public class Splice implements EffectsProcessor {
	
	// TODO implement cross fades
	
	public double[] process(double[]... buffers) {
		int samples = 0;
		for (double[] buffer : buffers) samples += buffer.length;
		double[] processedBuffer = new double[samples];
		for (int i = 0, j = 0, k = 0; i < samples; i++, k++) {
			processedBuffer[i] = buffers[j][k];
			if (k == buffers[j].length - 1) {
				k = 0;
				if (j < buffers.length - 1) j += 1;
			}
		}
		return processedBuffer;
	}

}
