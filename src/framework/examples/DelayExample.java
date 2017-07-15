package framework.examples;

import framework.effects.Delay;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class DelayExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/stereoLisa.wav").getMonoSum();
		new AudioTask().playMono(new Delay().process(lisa, 500, 0.5f, 0.5f));
	}

}
