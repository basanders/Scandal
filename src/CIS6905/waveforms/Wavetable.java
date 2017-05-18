package CIS6905.waveforms;

import CIS6905.PlotUtility;

public abstract class Wavetable extends Waveform {
	
	public final int tableSize;
	double[] wavetable;
	
	Wavetable(int size) {
		super(false);
		tableSize = size;
		wavetable = new double[tableSize];
	}

	public abstract void fillTable();
	
	@Override
	public void plot(double frequency, int samples) {
		double[] array = new double[samples];
		double oscFreq = tableSize * frequency / samples;
		double oscPhase = 0;
		for (int i = 0; i < samples; i++) {
			array[i] = getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= tableSize) oscPhase -= tableSize;
		}
		new PlotUtility(this.getClass().getSimpleName(), array);
	}
	
}
