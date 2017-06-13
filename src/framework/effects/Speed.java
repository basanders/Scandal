package framework.effects;

public class Speed implements EffectsProcessor {
	
	// TODO implement variable parameters
	
	public double[] process(double[] buffer, double speed) {
		if (speed == 0) return buffer;
		if (speed < 0) speed = -speed;
		int samples = (int) (buffer.length / speed);
		double[] processedBuffer = new double[samples];
		double speedIndex = 0;
		for (int i = 0; i < samples; i++) {
			processedBuffer[i] = buffer[(int) speedIndex];
			speedIndex += speed;
			if (speedIndex >= buffer.length) speedIndex -= buffer.length;
		}
		return processedBuffer;
	}

}
