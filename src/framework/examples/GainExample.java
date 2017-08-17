package framework.examples;

import framework.effects.Gain;
import framework.generators.AudioTask;
import framework.generators.BreakpointFunction;
import framework.generators.WaveFile;

public class GainExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/test.wav").getMonoSum();
		float[] envelope = new BreakpointFunction(200, new float[]{0.0f, 1.0f, 0.0f}).get();
		float[] gain = new Gain().process(lisa, envelope);
		new AudioTask().playMono(gain);
	}

}
