package framework.effects;

import framework.utilities.PlotUtility;
import framework.utilities.Settings;

public abstract class Biquad {

	private float b0;
	private float b1;
	private float b2;
	private float a1;
	private float a2;
	private float x1; // z^{-1} of input
	private float x2; // z^{-2} of input
	private float y1; // z^{-1} of output
	private float y2; // z^{-2} of output

	public abstract void update(float frequency, float gain);

	public void reset() {
		x1 = x2 = y1 = y2 = 0;
	}

	public void setCoefficients(float b0, float b1, float b2, float a0, float a1, float a2) {
		float a0Inverse = 1 / a0; // normalize
		this.b0 = b0 * a0Inverse;
		this.b1 = b1 * a0Inverse;
		this.b2 = b2 * a0Inverse;
		this.a1 = a1 * a0Inverse;
		this.a2 = a2 * a0Inverse;
	}

	public float[] process(float[] buffer, float frequency, float gain) {
		update(frequency, gain);
		float[] processedBuffer = new float[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			float x = buffer[i];
			float y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
		}
		return processedBuffer;
	}

	public float[] process(float[] buffer, float[] frequencies, float gain) {
		float frequencyIndex = 0;		
		float frequencyIncrement = (float) frequencies.length / buffer.length;
		float[] processedBuffer = new float[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			update(frequencies[(int) frequencyIndex], gain);
			float x = buffer[i];
			float y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
			frequencyIndex += frequencyIncrement;
		}
		return processedBuffer;
	}

	public float[] process(float[] buffer, float frequency, float[] gains) {
		float gainIndex = 0;
		float gainIncrement = (float) gains.length / buffer.length;
		float[] processedBuffer = new float[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			update(frequency, gains[(int) gainIndex]);
			float x = buffer[i];
			float y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
			gainIndex += gainIncrement;
		}
		return processedBuffer;
	}

	public float[] process(float[] buffer, float[] frequencies, float[] gains) {
		float frequencyIndex = 0;		
		float frequencyIncrement = (float) frequencies.length / buffer.length;
		float gainIndex = 0;
		float gainIncrement = (float) gains.length / buffer.length;
		float[] processedBuffer = new float[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			update(frequencies[(int) frequencyIndex], gains[(int) gainIndex]);
			float x = buffer[i];
			float y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
			frequencyIndex += frequencyIncrement;
			gainIndex += gainIncrement;
		}
		return processedBuffer;
	}

	private static float convertBadValuesToZero(float x) {
		if (Math.abs(x) > 1e-15 && Math.abs(x) < 1e15) return x;
		return 0.0f;
	}

	public static float normalizeAndClip(float value) {
		value *= (float) 2 / Settings.samplingRate; // normalize from 0 to 1 (Nyquist)
		return Math.max(0.0f, Math.min(value, 1.0f));
	}

	public void plotMagnitudeResponse(int length, float frequency, float gain) {
		update(frequency, gain);
		float[] data = new float[length];
		double w;
		double numerator;
		double denominator;
		float magnitude;
		for (int i = 0; i < length; i++) {
			w = (Math.PI * 2 * i) / Settings.samplingRate;  
			numerator = b0 * b0 + b1 * b1 + b2 * b2 + 2 * (b0 * b1 + b1 * b2) * Math.cos(w);
			numerator += 2 * b0 * b2 * Math.cos(2 * w);
			denominator = 1 + a1 * a1 + a2 * a2 + 2 * (a1 + a1 * a2) * Math.cos(w);
			denominator += 2 * a2 * Math.cos(2 * w);
			magnitude = (float) Math.sqrt(numerator / denominator);
			data[i] = magnitude;   
		}
		new PlotUtility(this.getClass().getSimpleName() + " magnitude response", data);
	}

}
