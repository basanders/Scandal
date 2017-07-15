package framework.examples;

import framework.effects.Loop;
import framework.effects.Splice;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class SpliceExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float[] loop1 = new Loop().process(lisa, 0, 12000, 8);
		float[] loop2 = new Loop().process(lisa, 0, 6000, 16);
		float[] loop3 = new Loop().process(lisa, 0, 3000, 32);
		float[] splice = new Splice().process(loop1, loop2, loop3);
		new AudioTask().playMono(splice);
	}

}
