package framework.examples;

import framework.generators.AudioFlow;
import framework.generators.KarplusStrong;
import framework.utilities.Settings;

public class KarplusStrongExample {
	
	public static void main(String[] args) throws Exception {
		KarplusStrong synth = new KarplusStrong(Settings.midiController);
		AudioFlow flow = synth.start();
		Thread.sleep(60000);
		flow.quit();
		synth.close();
		System.exit(0);
	}

}
