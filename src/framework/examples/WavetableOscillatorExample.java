package framework.examples;

import framework.generators.AudioFlow;
import framework.generators.WavetableOscillator;
import framework.waveforms.ClassicSquare;

public class WavetableOscillatorExample {

	public static void main(String[] args) throws Exception {
		WavetableOscillator saw = new WavetableOscillator(new ClassicSquare());
		AudioFlow flow = saw.start();
		saw.setFrequency(440);
		saw.setAmplitude(0.5f);
		for (int i = 0; i < 100; i++) {
			Thread.sleep((i * 100) % 500);
			saw.setFrequency((440 * i) % 2000);
		}
		flow.quit();
	}

}
