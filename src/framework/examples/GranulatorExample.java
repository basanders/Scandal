package framework.examples;

import framework.effects.Gain;
import framework.effects.Granulator;
import framework.generators.AudioTask;
import framework.generators.WaveFile;

public class GranulatorExample {
	
	// TODO real-time
	
	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float position = 2000;
		float deviation = 50;
		float speed = 1.6f;
		float grainLength = 50;
		float interGrainTime = 10f;
		float[] granulatedLisa = new Granulator().process(lisa, position, deviation, speed, grainLength, interGrainTime);
		new AudioTask().playMono(new Gain().process(granulatedLisa, 0.2f));
	}

}
