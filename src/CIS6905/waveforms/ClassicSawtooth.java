package CIS6905.waveforms;

public class ClassicSawtooth extends AliasedSawtooth implements ClassicWaveform {
	
	@Override
	public double getSample(double phase, double frequency) {
		double distance = 0;
		double index = 0;
		double residual = 0;
		if (phase > 1 - frequency) { // Handle left side of the discontinuity.
			distance = (phase - 1) / frequency;
			index = tableCenter * (distance + 1);
			residual = residuals[(int) index];
			// Inverted sawtooth waves have rising edges, so we add.
			return wavetable[(int) (phase * tableSize)] + residual;
		}
		if (phase < frequency) { // Handle right side of the discontinuity.
			distance = phase / frequency;
			index = tableCenter * (distance + 1) + 1;
			residual = residuals[(int) index];
			return wavetable[(int) (phase * tableSize)] + residual;
		}
		return wavetable[(int) (phase * tableSize)];
	}
	
	@Override
	public void plot(double frequency, int samples) {
		double[] array = new double[samples];
		double oscFreq = frequency / samples;
		double oscPhase = 0;
		for (int i = 0; i < samples; i++) {
			array[i] = getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= 1) oscPhase -= 1;
		}
		new PlotUtility(this.getClass().getSimpleName(), array);
	}

}
