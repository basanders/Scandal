package framework.generators;

import framework.utilities.PlotUtility;

public abstract class Function {

	public final int length;
	public final float twoPi = (float) Math.PI * 2;
	
	public Function(int length) {
		this.length = length;
	}
	
	public abstract float[] get();
	
	public void plot() {
		new PlotUtility(this.getClass().getSimpleName(), get());
	}

}
