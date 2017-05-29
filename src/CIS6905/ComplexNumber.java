package CIS6905;

public class ComplexNumber {
	
	private double x;
	private double y;
	
	public ComplexNumber(double x, double y) {
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
	
	public ComplexNumber add(ComplexNumber summand) {
		return new ComplexNumber(x + summand.x, y + summand.y);
	}
	
	public ComplexNumber subtract(ComplexNumber summand) {
		return new ComplexNumber(x - summand.x, y - summand.y);
	}
	
	public ComplexNumber multiplyBy(ComplexNumber factor) {
		return new ComplexNumber(x * factor.x - y * factor.y, x * factor.y + y * factor.x);
	}
	
	public ComplexNumber divideBy(ComplexNumber factor) {
		double denominator = factor.getMagnitude();
		double realPart = (x * factor.x + y * factor.y) / denominator;
		double imaginaryPart = (y * factor.x + x * factor.y) / denominator;
		return new ComplexNumber(realPart, imaginaryPart);
	}

}
