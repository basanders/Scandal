package framework.examples;

import framework.effects.Gain;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class GainExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float[] gain = new Gain().process(lisa, 0.5f);
		new AudioTask().playMono(gain);
	}

}
