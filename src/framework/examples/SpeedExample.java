package framework.examples;

import framework.effects.Speed;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class SpeedExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float[] speed = new Speed().process(lisa, 1.2f);
		new AudioTask().playMono(speed);
	}

}
