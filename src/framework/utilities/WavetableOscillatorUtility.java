package framework.utilities;

import framework.generators.NoiseGenerator;
import framework.generators.WavetableOscillator;
import framework.waveforms.AliasedTriangle;
import framework.waveforms.ClassicSawtooth;
import framework.waveforms.ClassicSquare;
import framework.waveforms.WavetableCosine;

public class WavetableOscillatorUtility {

	public float[] get(int duration, float amplitude, float frequency, int shape) {
		switch (shape) {
		case 2: return new WavetableOscillator(new ClassicSawtooth()).get(duration, amplitude, frequency);
		case 3: return new WavetableOscillator(new ClassicSquare()).get(duration, amplitude, frequency);
		case 4: return new WavetableOscillator(new AliasedTriangle()).get(duration, amplitude, frequency);
		case 5: return new NoiseGenerator().get(duration, amplitude);
		default: return new WavetableOscillator(new WavetableCosine()).get(duration, amplitude, frequency);
		}
	}

	public float[] get(int duration, float[] amplitude, float frequency, int shape) {
		switch (shape) {
		case 2: return new WavetableOscillator(new ClassicSawtooth()).get(duration, amplitude, frequency);
		case 3: return new WavetableOscillator(new ClassicSquare()).get(duration, amplitude, frequency);
		case 4: return new WavetableOscillator(new AliasedTriangle()).get(duration, amplitude, frequency);
		case 5: return new NoiseGenerator().get(duration, amplitude);
		default: return new WavetableOscillator(new WavetableCosine()).get(duration, amplitude, frequency);
		}
	}

	public float[] get(int duration, float amplitude, float[] frequency, int shape) {
		switch (shape) {
		case 2: return new WavetableOscillator(new ClassicSawtooth()).get(duration, amplitude, frequency);
		case 3: return new WavetableOscillator(new ClassicSquare()).get(duration, amplitude, frequency);
		case 4: return new WavetableOscillator(new AliasedTriangle()).get(duration, amplitude, frequency);
		case 5: return new NoiseGenerator().get(duration, amplitude);
		default: return new WavetableOscillator(new WavetableCosine()).get(duration, amplitude, frequency);
		}
	}

	public float[] get(int duration, float[] amplitude, float[] frequency, int shape) {
		switch (shape) {
		case 2: return new WavetableOscillator(new ClassicSawtooth()).get(duration, amplitude, frequency);
		case 3: return new WavetableOscillator(new ClassicSquare()).get(duration, amplitude, frequency);
		case 4: return new WavetableOscillator(new AliasedTriangle()).get(duration, amplitude, frequency);
		case 5: return new NoiseGenerator().get(duration, amplitude);
		default: return new WavetableOscillator(new WavetableCosine()).get(duration, amplitude, frequency);
		}
	}

}
