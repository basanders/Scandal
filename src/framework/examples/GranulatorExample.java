package framework.examples;

import framework.effects.Gain;
import framework.effects.Granulator;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class GranulatorExample {
	
	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float position = 2000;
		float deviation = 50;
		float grainLength = 150;
		float interGrainTime = 0.1f;
		float speed = 1.6f;
		float[] granulatedLisa = new Granulator().process(lisa, position, deviation, grainLength, interGrainTime, speed);
		new AudioTask().playMono(new Gain().process(granulatedLisa, 0.2f));
	}

}
