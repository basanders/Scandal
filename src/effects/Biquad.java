package effects;

import utilities.PlotUtility;
import utilities.Settings;

public abstract class Biquad implements EffectsProcessor {

	private double b0;
	private double b1;
	private double b2;
	private double a1;
	private double a2;
	private double x1; // z^{-1} of input
	private double x2; // z^{-2} of input
	private double y1; // z^{-1} of output
	private double y2; // z^{-2} of output

	public abstract void update(double frequency, double gain);

	public void reset() {
		x1 = x2 = y1 = y2 = 0;
	}

	public void setCoefficients(double b0, double b1, double b2, double a0, double a1, double a2) {
		double a0Inverse = 1 / a0; // normalize
		this.b0 = b0 * a0Inverse;
		this.b1 = b1 * a0Inverse;
		this.b2 = b2 * a0Inverse;
		this.a1 = a1 * a0Inverse;
		this.a2 = a2 * a0Inverse;
	}

	@Override
	public double[] process(double[] buffer, double frequency, double gain) {
		update(frequency, gain);
		double[] processedBuffer = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			double x = buffer[i];
			double y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double[] frequencies, double gain) {
		double frequencyIndex = 0;		
		double frequencyIncrement = (double) frequencies.length / buffer.length;
		double[] processedBuffer = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			update(frequencies[(int) frequencyIndex], gain);
			double x = buffer[i];
			double y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
			frequencyIndex += frequencyIncrement;
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double frequency, double[] gains) {
		double gainIndex = 0;
		double gainIncrement = (double) gains.length / buffer.length;
		double[] processedBuffer = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			update(frequency, gains[(int) gainIndex]);
			double x = buffer[i];
			double y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			processedBuffer[i] = y;
			this.x2 = convertBadValuesToZero(x1);
			this.x1 = convertBadValuesToZero(x);
			this.y2 = convertBadValuesToZero(y1);
			this.y1 = convertBadValuesToZero(y);
			gainIndex += gainIncrement;
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double[] frequencies, double[] gains) {
		double frequencyIndex = 0;		
		double frequencyIncrement = (double) frequencies.length / buffer.length;
		double gainIndex = 0;
		double gainIncrement = (double) gains.length / buffer.length;
		double[] processedBuffer = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			update(frequencies[(int) frequencyIndex], gains[(int) gainIndex]);
			double x = buffer[i];
			double y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
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

	private static double convertBadValuesToZero(double x) {
		if (Math.abs(x) > 1e-15 && Math.abs(x) < 1e15) return x;
		return 0.0;
	}

	public static double normalizeAndClip(double value) {
		value *= (double) 2 / Settings.samplingRate; // normalize from 0 to 1 (Nyquist)
		return Math.max(0.0, Math.min(value, 1.0));
	}

	public void plotMagnitudeResponse(int length, double frequency, double gain) {
		update(frequency, gain);
		double[] data = new double[length];
		double w;
		double numerator;
		double denominator;
		double magnitude;
		for (int i = 0; i < length; i++) {
			w = 2 * Math.PI * (double) i / Settings.samplingRate;  
			numerator = b0 * b0 + b1 * b1 + b2 * b2 + 2 * (b0 * b1 + b1 * b2) * Math.cos(w) + 2 * b0 * b2 * Math.cos(2 * w);
			denominator = 1 + a1 * a1 + a2 * a2 + 2 * (a1 + a1 * a2) * Math.cos(w) + 2 * a2 * Math.cos(2 * w);
			magnitude = Math.sqrt(numerator / denominator);
			data[i] = magnitude;   
		}
		new PlotUtility(this.getClass().getSimpleName() + " magnitude response", data);
	}

}
