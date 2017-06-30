package framework.generators;

import framework.utilities.PlotUtility;

public class BreakpointFunction {
	
	private final int length;
	public final double[] breakpoints;
	public final double[] durations;
	public final double[] weights;
	
	// TODO add more constructors
	// TODO Exponential functions
	
	public BreakpointFunction(int length, double[] breakpoints) {
		this.length = length; // between each breakpoint
		this.breakpoints = breakpoints;
		this.durations = null;
		this.weights = null;
	}
	
	public BreakpointFunction(int length, double[] breakpoints, double[] durations, double[] weights) {
		this.length = length; // between each breakpoint
		this.breakpoints = breakpoints;
		this.durations = durations;
		this.weights = weights;
	}
	
	public double[] get() {
		double[] array = new double[length * (breakpoints.length - 1)];
		int index = 0;
		double value = breakpoints[0];
		double increment = 0;
		for (int i = 0; i < breakpoints.length - 1; i++) {
			for (int j = 0; j < length; j++) {
				if (i != breakpoints.length - 2) {
					increment = (breakpoints[i + 1] - breakpoints[i]) / length;
				} else {
					increment = (breakpoints[i + 1] - breakpoints[i]) / (length - 1);
				}
				array[index] = value;
				index += 1;
				value += increment;
			}
		}
		return array;
	}
	
	public void plot() {
		new PlotUtility(this.getClass().getSimpleName(), get());
	}

}
