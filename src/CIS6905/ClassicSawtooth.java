package CIS6905;

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

}
