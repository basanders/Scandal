package framework.effects;

public class Speed implements EffectsProcessor {
	
	// TODO implement variable parameters
	
	public float[] process(float[] buffer, int speed) {
		return process(buffer, (float) speed);
	}
	
	public float[] process(float[] buffer, float speed) {
		if (speed == 0) return buffer;
		if (speed < 0) speed = -speed;
		int samples = (int) (buffer.length / speed);
		float[] processedBuffer = new float[samples];
		float speedIndex = 0;
		for (int i = 0; i < samples; i++) {
			processedBuffer[i] = buffer[(int) speedIndex];
			speedIndex += speed;
			if (speedIndex >= buffer.length) speedIndex -= buffer.length;
		}
		return processedBuffer;
	}

}
