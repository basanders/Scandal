package effects;

public class BiquadBandPass extends Biquad {

	@Override
	public void update(double frequency, double filterQ) {
		frequency = normalizeAndClip(frequency);
		if (frequency > 0 && frequency < 1) {
			if (filterQ > 0) {
				double w0 = Math.PI * frequency;
				double alpha = Math.sin(w0) / (2 * filterQ);
				double k = Math.cos(w0);
				double b0 = alpha;
				double b1 = 0;
				double b2 = -alpha;
				double a0 = 1 + alpha;
				double a1 = -2 * k;
				double a2 = 1 - alpha;
				setCoefficients(b0, b1, b2, a0, a1, a2);
			} else {
				// When filterQ = 0, the z-transform gives lim_{Q -> 0} = 1.
				setCoefficients(1, 0, 0, 1, 0, 0);
			}
		} else {
			setCoefficients(0, 0, 0, 1, 0, 0);
		}
	}

}
