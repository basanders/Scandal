package framework.effects;

public class Loop implements EffectsProcessor {

	public float[] process(float[] buffer, int start, int end) {
		int count = buffer.length / (end - start);
		return process(buffer, start, end, count); // try to preserve the duration
	}

	public float[] process(float[] buffer, int start, int end, int count) {
		int samples = (end - start) * count;
		float[] processedBuffer = new float[samples];
		for (int i = 0, j = 0; i < samples; i++, j++) {
			processedBuffer[i] = buffer[j % end + start];
		}
		return processedBuffer;
	}

}
