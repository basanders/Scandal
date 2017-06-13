package framework.effects;

import framework.utilities.Complex;

public class BiquadCustom extends Biquad {

	private Complex pole;
	private Complex zero;

	public BiquadCustom(Complex pole) {
		this.pole = pole;
		this.zero = new Complex(1, 0).divideBy(pole);
		update(Double.NaN, Double.NaN);
	}

	public BiquadCustom(Complex pole, Complex zero) {
		this.pole = pole;
		this.zero = zero;
		update(Double.NaN, Double.NaN);
	}
	
	public void setPole(Complex pole) {
		this.pole = pole;
		update(Double.NaN, Double.NaN);
	}
	
	public void setZero(Complex zero) {
		this.zero = zero;
		update(Double.NaN, Double.NaN);
	}

	@Override
	public void update(double cutoff, double resonance) {
		double b0 = 1;
		double b1 = -2 * zero.getRealPart();
		double b2 = zero.getMagnitude() * zero.getMagnitude();
		double a1 = -2 * pole.getRealPart();
		double a2 = pole.getMagnitude() * pole.getMagnitude();
		setCoefficients(b0, b1, b2, 1, a1, a2);
	}

	public void plotMagnitudeResponse(int length) {
		plotMagnitudeResponse(length, Double.NaN, Double.NaN);
	}

}
