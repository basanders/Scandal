package framework.examples;

import framework.effects.BiquadLowPass;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class BiquadExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/stereoLisa.wav").getMonoSum();
		float[] filter = new BiquadLowPass().process(lisa, 1000, 1);
		new AudioTask().playMono(filter);
	}

}
