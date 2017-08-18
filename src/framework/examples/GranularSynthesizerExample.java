package framework.examples;

import framework.generators.AudioFlow;
import framework.generators.GranularSynthesizer;
import framework.utilities.Settings;
import framework.waveforms.ClassicSawtooth;

public class GranularSynthesizerExample {

	public static void main(String[] args) throws Exception {
		GranularSynthesizer synth = new GranularSynthesizer(Settings.midiController, new ClassicSawtooth());
		AudioFlow flow = synth.start();
		Thread.sleep(30000);
		flow.quit();
		synth.close();
		System.exit(0);
	}

}
