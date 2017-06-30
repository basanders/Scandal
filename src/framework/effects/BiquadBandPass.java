package framework.effects;

public class BiquadBandPass extends Biquad {

	@Override
	public void update(float frequency, float filterQ) {
		frequency = normalizeAndClip(frequency);
		if (frequency > 0 && frequency < 1) {
			if (filterQ > 0) {
				float w0 = (float) Math.PI * frequency;
				float alpha = (float) Math.sin(w0) / (2 * filterQ);
				float k = (float) Math.cos(w0);
				float b0 = alpha;
				float b1 = 0;
				float b2 = -alpha;
				float a0 = 1 + alpha;
				float a1 = -2 * k;
				float a2 = 1 - alpha;
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
