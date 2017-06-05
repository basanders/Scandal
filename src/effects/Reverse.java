package effects;

public class Reverse implements EffectsProcessor {
	
	public double[] process(double[] buffer) {
		double[] processedBuffer = new double[buffer.length];
		for (int i = 1; i <= buffer.length; i++) {
			processedBuffer[i - 1] = buffer[buffer.length - i];
		}
		return processedBuffer;
	}

}
