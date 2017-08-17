package framework.generators;

public class BreakpointFunction extends Function {
	
	public final float[] breakpoints;
	public final float[] durations;
	public final float[] weights;
	
	// TODO add more constructors
	// TODO Exponential functions
	
	public BreakpointFunction(int length, float[] breakpoints) {
		super(length); // between each breakpoint
		this.breakpoints = breakpoints;
		this.durations = null;
		this.weights = null;
	}
	
	@Override
	public float[] get() {
		float[] array = new float[length * (breakpoints.length - 1)];
		int index = 0;
		float value = breakpoints[0];
		float increment = 0;
		for (int i = 0; i < breakpoints.length - 1; i++) {
			for (int j = 0; j < length; j++) {
				if (i != breakpoints.length - 2) increment = (breakpoints[i + 1] - breakpoints[i]) / length;
				else increment = (breakpoints[i + 1] - breakpoints[i]) / (length - 1);
				array[index] = value;
				index += 1;
				value += increment;
			}
		}
		return array;
	}

}
