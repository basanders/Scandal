package effects;

public class BiquadHiShelf extends Biquad {

	@Override
	public void update(double frequency, double gain) {
		frequency = normalizeAndClip(frequency);
		double A = Math.pow(10.0, gain * 0.05); // dB to linear
		if (frequency == 1) {
			setCoefficients(1, 0, 0, 1, 0, 0); // The z-transform is 1.
		} else if (frequency > 0) {
			double w0 = Math.PI * frequency;
			double S = 1; // filter slope (1 is the maximum value)
			double alpha = 0.5 * Math.sin(w0) * Math.sqrt((A + 1 / A) * (1 / S - 1) + 2);
			double k = Math.cos(w0);
			double k2 = 2 * Math.sqrt(A) * alpha;
			double aPlusOne = A + 1;
			double aMinusOne = A - 1;
			double b0 = A * (aPlusOne + aMinusOne * k + k2);
			double b1 = -2 * A * (aMinusOne + aPlusOne * k);
			double b2 = A * (aPlusOne + aMinusOne * k - k2);
			double a0 = aPlusOne - aMinusOne * k + k2;
			double a1 = 2 * (aMinusOne - aPlusOne * k);
			double a2 = aPlusOne - aMinusOne * k - k2;
			setCoefficients(b0, b1, b2, a0, a1, a2);
		} else {
			// When the frequency = 0, the filter adjusts the gain by A^2.
			setCoefficients(A * A, 0, 0, 1, 0, 0);
		}
	}

}
