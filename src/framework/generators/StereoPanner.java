package framework.generators;

public class StereoPanner {

	public float[] process(float[] buffer, float value) {
		if (Math.abs(value) > 1) value /= value; // sanity check
		float left = (float) Math.cos(Math.PI * (value + 1) / 4);
		float right = (float) Math.sin(Math.PI * (value + 1) / 4);
		int samples = buffer.length * 2;
		float[] interleavedBuffer = new float[samples];
		for (int i = 0, j = 0; i < samples; i += 2, j++) {
			interleavedBuffer[i] = left * buffer[j];
			interleavedBuffer[i + 1] = right * buffer[j];
		}
		return interleavedBuffer;
	}

	public float[] process(float[] buffer, float[] values) {
		int samples = buffer.length * 2;
		float[] interleavedBuffer = new float[samples];
		float left = 0;
		float right = 0;
		float valueIndex = 0;
		float valueIncrement = (float) values.length / buffer.length;
		for (int i = 0, j = 0; i < samples; i += 2, j++) {
			left = (float) Math.cos(Math.PI * (values[(int) valueIndex] + 1) / 4);
			right = (float) Math.sin(Math.PI * (values[(int) valueIndex] + 1) / 4);
			interleavedBuffer[i] = left * buffer[j];
			interleavedBuffer[i + 1] = right * buffer[j];
			valueIndex += valueIncrement;
		}
		return interleavedBuffer;
	}

}
