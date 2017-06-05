package effects;

public class Loop implements EffectsProcessor {

	// TODO implement variable parameters

	public double[] process(double[] buffer, int start, int end) {
		int count = buffer.length / (end - start);
		return process(buffer, start, end, count); // try to preserve the duration
	}

	public double[] process(double[] buffer, int start, int end, int count) {
		int samples = (end - start) * count;
		double[] processedBuffer = new double[samples];
		for (int i = 0, j = 0; i < samples; i++, j++) {
			processedBuffer[i] = buffer[j % end + start];
		}
		return processedBuffer;
	}

}
