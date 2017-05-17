package CIS6905.waveforms;

public class ClassicSquare extends AliasedSquare implements ClassicWaveform {

	@Override
	public double getSample(double phase, double frequency) {
		double distance = 0;
		double index = 0;
		double residual = 0;		
		// Handle the discontinuity between center = length / 2 - 1 and center + 1.
		if (phase > 0.5 - frequency && phase < 0.5 + frequency) {
			distance = (phase - 0.5) / frequency;
			index = tableCenter * (distance + 1);
			if (phase >= 0.5) index += 1;
			residual = residuals[(int) index];
			if (phase == 0.5) residual *= 0.25; // This is a kludge.
			// This is a falling edge from 1 to -1, so we subtract.
			return wavetable[(int) (phase * tableSize)] - residual;
		}
		// Handle the discontinuity between the first and last indices of the table.
		if (phase > 1 - frequency) { // Handle left side of the discontinuity.
			distance = (phase - 1) / frequency;
			index = tableCenter * (distance + 1);
			residual = residuals[(int) index];
			// This is a rising edge from -1 to 1, so we add.
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
