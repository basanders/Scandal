package framework.effects;

public class Gain implements EffectsProcessor {
	
	public double[] process(double[] buffer, double gain) {
		double[] processedBuffer = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = gain * buffer[i];
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double[] gains) {
		double[] processedBuffer = new double[buffer.length];
		double gainIndex = 0;
		double gainIncrement = (double) gains.length / buffer.length;
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = gains[(int) gainIndex] * buffer[i];
			gainIndex += gainIncrement;
		}
		return processedBuffer;
	}

}
