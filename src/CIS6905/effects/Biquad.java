package CIS6905.effects;

import CIS6905.ComplexNumber;
import CIS6905.PlotUtility;
import CIS6905.Settings;

public abstract class Biquad {

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
		value *= (double) 2 / Settings.samplingRate;
		return Math.max(0.0, Math.min(value, 1.0));
	}

	/*
	 * Frequencies are normalized from 0 to 1 (Nyquist). The filter equation is:
	 * 
	 * y[n] + m_a1 * y[n - 1] + m_a2 * y[n - 2] = m_b0 * x[n] + m_b1 * x[n - 1] + m_b2 * x[n - 2]
	 * 
	 * The z-transform is:
	 * 
	 * H(z) = (b0 + b1 * z^{-1} + b2 * z^{-2}) / (1 + a1 * z^{-1} + a2 * z^{-2})
	 *      = (b0 + (b1 + b2 * z^{-1}) * z^{-1}) / (1 + (a1 + a2 * z^{-1}) * z^{-1}),
	 * 
	 * where z = e^{i * pi * frequency}
	 */
	private ComplexNumber[] getFrequencyResponse(int length, double frequency, double gain) {
		update(frequency, gain);
		ComplexNumber[] frequencyResponse = new ComplexNumber[length];
		for (int i = 0; i < length; i++) {
			double angle = -Math.PI * (double) i / length;
			ComplexNumber z = new ComplexNumber(Math.cos(angle), Math.sin(angle));
			ComplexNumber parenthesis = new ComplexNumber(b1, 0).add(new ComplexNumber(b2, 0).multiplyBy(z));
			ComplexNumber numerator = new ComplexNumber(b0, 0).add(parenthesis.multiplyBy(z));
			parenthesis = new ComplexNumber(a1, 0).add(new ComplexNumber(a2, 0).multiplyBy(z));
			ComplexNumber denominator = new ComplexNumber(1, 0).add(parenthesis.multiplyBy(z));
			frequencyResponse[i] = numerator.divideBy(denominator);
		}
		return frequencyResponse;
	}

	private double[] getMagnitudeResponse(int length, double frequency, double gain) {
		ComplexNumber[] frequencyResponse = getFrequencyResponse(length, frequency, gain);
		double[] magnitudeResponse = new double[frequencyResponse.length];
		for (int i = 0; i < frequencyResponse.length; i++) {
			magnitudeResponse[i] = frequencyResponse[i].getMagnitude();
		}
		return magnitudeResponse;
	}

	private double[] getPhaseResponse(int length, double frequency, double gain) {
		ComplexNumber[] frequencyResponse = getFrequencyResponse(length, frequency, gain);
		double[] phaseResponse = new double[frequencyResponse.length];
		for (int i = 0; i < frequencyResponse.length; i++) {
			phaseResponse[i] = Math.atan2(frequencyResponse[i].getImaginaryPart(), frequencyResponse[i].getRealPart());
		}
		return phaseResponse;
	}

	// TODO make this plot logarithmic
	public void plotMagnitudeResponse(int length, double frequency, double gain) {
		new PlotUtility(this.getClass().getSimpleName() + " magnitude response", getMagnitudeResponse(length, frequency, gain));
	}

	public void plotPhaseResponse(int length, double frequency, double gain) {
		new PlotUtility(this.getClass().getSimpleName() + " phase response", getPhaseResponse(length, frequency, gain));
	}

}
