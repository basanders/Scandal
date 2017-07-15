package framework.examples;

import framework.effects.RingModulator;
import framework.generators.AudioTask;
import framework.generators.WaveFile;
import framework.waveforms.WavetableCosine;

public class RingModulatorExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/stereoLisa.wav").getMonoSum();
		RingModulator tremolo = new RingModulator(new WavetableCosine());
		new AudioTask().playMono(tremolo.process(lisa, 0.8f, 10));
	}

}
