package framework.examples;

import framework.generators.AudioTask;
import framework.generators.BreakpointFunction;
import framework.generators.WavetableOscillator;
import framework.waveforms.ClassicSawtooth;

public class BreakpointFunctionExample {

	public static void main(String[] args) {
		float[] longEnvelope = new BreakpointFunction(512, new float[]{0, 0.5f, 0, 1, 0, 0.5f, 0}).get();
		float[] glide = new BreakpointFunction(512, new float[]{880, 110, 2200, 2200}).get();
		float[] saw = new WavetableOscillator(new ClassicSawtooth()).get(3000, longEnvelope, glide);
		new AudioTask().playMono(saw);
	}

}
