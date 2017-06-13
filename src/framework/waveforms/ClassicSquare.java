package framework.waveforms;

public class ClassicSquare extends AliasedSquare {
	
	static final double[] residuals = WavetableResidual.getSharedInstance().wavetable;
	static final int tableCenter = residuals.length / 2 - 1;

	@Override
	public double getSample(double phase, double frequency) {
		double distance = 0;
		double index = 0;
		double residual = 0;
		double halfSize = 0.5 * tableSize;
		// Handle the discontinuity between center = length / 2 - 1 and center + 1.
		if (phase > halfSize - frequency && phase < halfSize + frequency) {
			distance = (phase - halfSize) / frequency;
			index = tableCenter * (distance + 1);
			if (phase >= halfSize) index += 1;
			residual = residuals[(int) index];
			if (phase == halfSize) residual *= 0.25; // This is a kludge.
			// This is a falling edge from 1 to -1, so we subtract.
			return wavetable[(int) phase] - residual;
		}
		// Handle the discontinuity between the first and last indices of the table.
		if (phase > tableSize - frequency) { // Handle left side of the discontinuity.
			distance = (phase - tableSize) / frequency;
			index = tableCenter * (distance + 1);
			residual = residuals[(int) index];
			// This is a rising edge from -1 to 1, so we add.
			return wavetable[(int) phase] + residual;
		}
		if (phase < frequency) { // Handle right side of the discontinuity.
			distance = phase / frequency;
			index = tableCenter * (distance + 1) + 1;
			residual = residuals[(int) index];
			return wavetable[(int) phase] + residual;
		}
		return wavetable[(int) phase];
	}

}
