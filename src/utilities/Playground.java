package utilities;

import generators.AudioFlow;
import generators.PolyphonicSynthesizer;

public class Playground {

	public static void main(String[] args) throws Exception {
		PolyphonicSynthesizer synth = new PolyphonicSynthesizer(1);
		AudioFlow flow = synth.start();
		Thread.sleep(10000);
		flow.quit();
		synth.close();
		System.exit(0);
	}

}
