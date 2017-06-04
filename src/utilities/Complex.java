package utilities;

public class Complex {

	private double x;
	private double y;

	public Complex(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getRealPart() {
		return x;
	}

	public double getImaginaryPart() {
		return y;
	}

	public double getMagnitude() {
		return Math.sqrt(x * x + y * y);
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
		double denominator = factor.getMagnitude();
		double realPart = (x * factor.x + y * factor.y) / denominator;
		double imaginaryPart = (y * factor.x + x * factor.y) / denominator;
		return new Complex(realPart, imaginaryPart);
	}

}
