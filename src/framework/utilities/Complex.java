package framework.utilities;

public class Complex {

	private float x;
	private float y;

	public Complex(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getRealPart() {
		return x;
	}

	public float getImaginaryPart() {
		return y;
	}

	public float getMagnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Complex add(Complex summand) {
		return new Complex(x + summand.x, y + summand.y);
	}

	public Complex subtract(Complex summand) {
		return new Complex(x - summand.x, y - summand.y);
	}

	public Complex multiplyBy(Complex factor) {
		return new Complex(x * factor.x - y * factor.y, x * factor.y + y * factor.x);
	}

	public Complex divideBy(Complex factor) {
		float denominator = factor.getMagnitude();
		float realPart = (x * factor.x + y * factor.y) / denominator;
		float imaginaryPart = (y * factor.x + x * factor.y) / denominator;
		return new Complex(realPart, imaginaryPart);
	}

}
