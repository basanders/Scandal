package framework.examples;

import framework.effects.Loop;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class LoopExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float[] loop = new Loop().process(lisa, 0, 10000, 8);
		new AudioTask().playMono(loop);
	}

}
