package framework.examples;

import framework.generators.AudioTask;
import framework.generators.BreakpointFunction;
import framework.generators.WavetableOscillator;
import framework.waveforms.ClassicSawtooth;
import framework.waveforms.ClassicSquare;
import framework.waveforms.WavetableCosine;

public class AudioTaskScheduleExample {

	public static void main(String[] args) {
		float[] envelope = new BreakpointFunction(512, new float[]{0, 1, 0}).get();
		float[] longEnvelope = new BreakpointFunction(512, new float[]{0, 0.5f, 0, 1, 0, 0.5f, 0}).get();
		float[] glide = new BreakpointFunction(512, new float[]{880, 55, 2200, 1100, 4400}).get();
		WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
		WavetableOscillator square = new WavetableOscillator(new ClassicSquare());
		WavetableOscillator sine = new WavetableOscillator(new WavetableCosine());
		AudioTask task = new AudioTask(10); // use 10 threads
		float[] buffer;
		for (int i = 0; i < 200; i++) {
			buffer = sine.get(30, envelope, (float) Math.abs((i + 2) * 440 / 2 * Math.pow(-1, i)) % 2000);
			task.playMono((2 * i) * 90, buffer);
			buffer = saw.get(30, envelope, (float) Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
			task.playMono((2 * i + 1) * 90, buffer);
			buffer = square.get(30, envelope, (float) Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
			task.playMono((2 * i + 2) * 90, buffer);
			if (i == 150) {
				buffer = new WavetableOscillator(new ClassicSquare()).get(9999, longEnvelope, glide);
				task.playMono(2 * i * 90, buffer);
			}
		}
		task.stop(); // necessary whenever managing more than one thread
	}

}
