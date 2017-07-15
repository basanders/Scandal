package framework.examples;

import framework.generators.AudioFlow;
import framework.generators.PolyphonicSynthesizer;
import framework.utilities.Settings;
import framework.waveforms.ClassicSawtooth;

public class PolyphonicSynthesizerExample {

	public static void main(String[] args) throws Exception {
		int keyboard = Settings.midiController;
		PolyphonicSynthesizer synth = new PolyphonicSynthesizer(keyboard, new ClassicSawtooth());
		AudioFlow flow = synth.start();
		Thread.sleep(10000);
		flow.quit();
		synth.close();
		System.exit(0);
	}

}
