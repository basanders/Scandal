package CIS6905.waveforms;

import CIS6905.utilities.PlotUtility;

public abstract class Waveform {
	
	public final double twoPi = 2 * Math.PI;
	public final boolean isNaive;
	
	Waveform(boolean isNaive) {
		this.isNaive = isNaive;
	}
	
	public abstract double getSample(double phase, double frequency);
	
	public void plot(double frequency, int samples) {
		double[] array = new double[samples];
		double oscFreq = twoPi * frequency / samples;
		double oscPhase = 0;
		for (int i = 0; i < samples; i++) {
			array[i] = getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= twoPi) oscPhase -= twoPi;
		}
		new PlotUtility(this.getClass().getSimpleName(), array);
	}

}
