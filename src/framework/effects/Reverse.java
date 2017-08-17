package framework.effects;

public class Reverse {
	
	public float[] process(float[] buffer) {
		float[] processedBuffer = new float[buffer.length];
		for (int i = 1; i <= buffer.length; i++) {
			processedBuffer[i - 1] = buffer[buffer.length - i];
		}
		return processedBuffer;
	}

}
