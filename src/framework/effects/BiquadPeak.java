package framework.effects;

public class BiquadPeak extends Biquad {
	
	@Override
	public void update(float frequency, float gain) {
		update(frequency, gain, 1);
	}

	void update(float frequency, float gain, float filterQ) {
		frequency = normalizeAndClip(frequency);
		float A = (float) Math.pow(10.0, gain * 0.05); // dB to linear
		if (frequency > 0 && frequency < 1) {
			if (filterQ > 0) {
				float w0 = (float) Math.PI * frequency;
				float alpha = (float) Math.sin(w0) / (2 * filterQ);
				float k = (float) Math.cos(w0);
				float b0 = 1 + alpha * A;
				float b1 = -2 * k;
				float b2 = 1 - alpha * A;
				float a0 = 1 + alpha / A;
				float a1 = -2 * k;
				float a2 = 1 - alpha / A;
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
