package framework.effects;

public class BiquadHiPass extends Biquad {

	@Override
	public void update(float cutoff, float resonance) {
		cutoff = normalizeAndClip(cutoff);
		if (cutoff == 1) {
			setCoefficients(0, 0, 0, 1, 0, 0); // When cutoff is 1, the z-transform is 0.
		} else if (cutoff > 0) {
			float g = (float) Math.pow(10, resonance * 0.05); // dB to linear
			float d = (float) Math.sqrt((4 - Math.sqrt(16 - 16 / (g * g))) / 2);
			float theta = (float) Math.PI * cutoff;
			float sn = 0.5f * d * (float) Math.sin(theta);
			float beta = 0.5f * (1 - sn) / (1 + sn);
			float gamma = (0.5f + beta) * (float) Math.cos(theta);
			float alpha = 0.25f * (0.5f + beta + gamma);
			float b0 = 2 * alpha;
			float b1 = 2 * -2 * alpha;
			float b2 = 2 * alpha;
			float a1 = 2 * -gamma;
			float a2 = 2 * beta;
			setCoefficients(b0, b1, b2, 1, a1, a2);
		} else {
			setCoefficients(1, 0, 0, 1, 0, 0); // When cutoff is zero, the z-transform is 1.
		}
	}

}
