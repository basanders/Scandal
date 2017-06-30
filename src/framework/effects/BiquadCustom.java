package framework.effects;

import framework.utilities.Complex;

public class BiquadCustom extends Biquad {

	private Complex pole;
	private Complex zero;

	public BiquadCustom(Complex pole) {
		this.pole = pole;
		this.zero = new Complex(1, 0).divideBy(pole);
		update(Float.NaN, Float.NaN);
	}

	public BiquadCustom(Complex pole, Complex zero) {
		this.pole = pole;
		this.zero = zero;
		update(Float.NaN, Float.NaN);
	}
	
	public void setPole(Complex pole) {
		this.pole = pole;
		update(Float.NaN, Float.NaN);
	}
	
	public void setZero(Complex zero) {
		this.zero = zero;
		update(Float.NaN, Float.NaN);
	}

	@Override
	public void update(float cutoff, float resonance) {
		float b0 = 1;
		float b1 = -2 * zero.getRealPart();
		float b2 = zero.getMagnitude() * zero.getMagnitude();
		float a1 = -2 * pole.getRealPart();
		float a2 = pole.getMagnitude() * pole.getMagnitude();
		setCoefficients(b0, b1, b2, 1, a1, a2);
	}

	public void plotMagnitudeResponse(int length) {
		plotMagnitudeResponse(length, Float.NaN, Float.NaN);
	}

}
