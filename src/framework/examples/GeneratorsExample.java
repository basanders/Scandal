package framework.examples;

import framework.generators.AudioTask;
import framework.generators.NaiveOscillator;
import framework.generators.NoiseGenerator;
import framework.generators.WavetableOscillator;
import framework.waveforms.ClassicSawtooth;
import framework.waveforms.NaiveSawtooth;
import framework.waveforms.WavetableWhite;

public class GeneratorsExample {
	
	public static void main(String[] args) {
		new AudioTask().playMono(0, new NoiseGenerator(new WavetableWhite()).get(2000, 0.5f));
		new AudioTask().playMono(2000, new NaiveOscillator(new NaiveSawtooth()).get(2000, 0.5f, 440));
		new AudioTask().playMono(4000, new WavetableOscillator(new ClassicSawtooth()).get(2000, 0.5f, 440));
	}
	
}
