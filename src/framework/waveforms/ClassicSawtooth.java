package framework.waveforms;

public class ClassicSawtooth extends AliasedSawtooth {
	
	static final double[] residuals = WavetableResidual.getSharedInstance().wavetable;
	static final int tableCenter = residuals.length / 2 - 1;
	
	@Override
	public double getSample(double phase, double frequency) {
		double distance = 0;
		double index = 0;
		double residual = 0;
		if (phase > tableSize - frequency) { // Handle left side of the discontinuity.
			distance = (phase - tableSize) / frequency;
			index = tableCenter * (distance + 1);
			residual = residuals[(int) index];
			// Inverted sawtooth waves have rising edges, so we add.
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
