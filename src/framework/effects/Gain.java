package framework.effects;

public class Gain implements EffectsProcessor {
	
	public float[] process(float[] buffer, int gain) {
		return process(buffer, (float) gain);
	}
	
	public float[] process(float[] buffer, float gain) {
		float[] processedBuffer = new float[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = gain * buffer[i];
		}
		return processedBuffer;
	}

	public float[] process(float[] buffer, float[] gains) {
		float[] processedBuffer = new float[buffer.length];
		float gainIndex = 0;
		float gainIncrement = (float) gains.length / buffer.length;
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = gains[(int) gainIndex] * buffer[i];
			gainIndex += gainIncrement;
		}
		return processedBuffer;
	}

}
