package framework.waveforms;

import framework.utilities.PlotUtility;

public abstract class Waveform {

	public final float twoPi = (float) Math.PI * 2;
	public final boolean isNaive;

	Waveform(boolean isNaive) {
		this.isNaive = isNaive;
	}

	public abstract float getSample(float phase, float frequency);

	public float[] getTable(int samples, float frequency) {
		float[] array = new float[samples];
		float oscFreq = twoPi * frequency / samples;
		float oscPhase = 0;
		for (int i = 0; i < samples; i++) {
			array[i] = getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= twoPi) oscPhase -= twoPi;
		}
		return array;
	}

	public void plot(int samples, float frequency) {
		new PlotUtility(this.getClass().getSimpleName(), getTable(samples, frequency));
	}

}
