package CIS6905.effects;

public class BiquadPeak extends Biquad {
	
	@Override
	public void update(double frequency, double gain) {
		update(frequency, gain, 1);
	}

	void update(double frequency, double gain, double filterQ) {
		frequency = normalizeAndClip(frequency);
		double A = Math.pow(10.0, gain * 0.05); // dB to linear
		if (frequency > 0 && frequency < 1) {
			if (filterQ > 0) {
				double w0 = Math.PI * frequency;
				double alpha = Math.sin(w0) / (2 * filterQ);
				double k = Math.cos(w0);
				double b0 = 1 + alpha * A;
				double b1 = -2 * k;
				double b2 = 1 - alpha * A;
				double a0 = 1 + alpha / A;
				double a1 = -2 * k;
				double a2 = 1 - alpha / A;
				setCoefficients(b0, b1, b2, a0, a1, a2);
			} else {
				// When filterQ = 0, the z-transform gives lim_{filterQ -> 0} = A^2.
				setCoefficients(A * A, 0, 0, 1, 0, 0);
			}
		} else {
			setCoefficients(1, 0, 0, 1, 0, 0); // When frequency is 0 or 1, the z-transform is 1.
		}
	}

}
