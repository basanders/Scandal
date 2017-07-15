package framework.examples;

import framework.effects.Reverse;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class ReverseExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float[] reverse = new Reverse().process(lisa);
		new AudioTask().playMono(reverse);
	}

}
