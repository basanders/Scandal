package CIS6905.effects;

public class BiquadHiPass extends Biquad {

	@Override
	public void update(double cutoff, double resonance) {
		cutoff = normalizeAndClip(cutoff);
		if (cutoff == 1) {
			setCoefficients(0, 0, 0, 1, 0, 0); // When cutoff is 1, the z-transform is 0.
		} else if (cutoff > 0) {
			double g = Math.pow(10, resonance * 0.05); // dB to linear
			double d = Math.sqrt((4 - Math.sqrt(16 - 16 / (g * g))) / 2);
			double theta = Math.PI * cutoff;
			double sn = 0.5 * d * Math.sin(theta);
			double beta = 0.5 * (1 - sn) / (1 + sn);
			double gamma = (0.5 + beta) * Math.cos(theta);
			double alpha = 0.25 * (0.5 + beta + gamma);
			double b0 = 2 * alpha;
			double b1 = 2 * -2 * alpha;
			double b2 = 2 * alpha;
			double a1 = 2 * -gamma;
			double a2 = 2 * beta;
			setCoefficients(b0, b1, b2, 1, a1, a2);
		} else {
			setCoefficients(1, 0, 0, 1, 0, 0); // When cutoff is zero, the z-transform is 1.
		}
	}

}
