package framework.effects;

public class Splice {
	
	// TODO implement cross fades
	
	public float[] process(float[]... buffers) {
		int samples = 0;
		for (float[] buffer : buffers) samples += buffer.length;
		float[] processedBuffer = new float[samples];
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
