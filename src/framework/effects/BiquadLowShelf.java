package framework.effects;

public class BiquadLowShelf extends Biquad {

	@Override
	public void update(float frequency, float gain) {
		frequency = normalizeAndClip(frequency);
		float A = (float) Math.pow(10.0, gain * 0.05); // dB to linear
		if (frequency == 1) {
			setCoefficients(A * A, 0, 0, 1, 0, 0); // The filter adjusts the gain by A^2.
		} else if (frequency > 0) {
			float w0 = (float) Math.PI * frequency;
			float S = 1; // filter slope (1 is the maximum value)
			float alpha = 0.5f * (float) Math.sin(w0) * (float) Math.sqrt((A + 1 / A) * (1 / S - 1) + 2);
			float k = (float) Math.cos(w0);
			float k2 = 2 * (float) Math.sqrt(A) * alpha;
			float aPlusOne = A + 1;
			float aMinusOne = A - 1;
			float b0 = A * (aPlusOne - aMinusOne * k + k2);
			float b1 = 2 * A * (aMinusOne - aPlusOne * k);
			float b2 = A * (aPlusOne - aMinusOne * k - k2);
			float a0 = aPlusOne + aMinusOne * k + k2;
			float a1 = -2 * (aMinusOne + aPlusOne * k);
			float a2 = aPlusOne + aMinusOne * k - k2;
			setCoefficients(b0, b1, b2, a0, a1, a2);
		} else {
			setCoefficients(1, 0, 0, 1, 0, 0); // When frequency is 0, the z-transform is 1.
		}
	}

}
