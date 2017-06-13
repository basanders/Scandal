package framework.utilities;

import framework.generators.AudioFlow;
import framework.generators.KarplusStrong;

public class Playground {

	public static void main(String[] args) throws Exception {
		KarplusStrong synth = new KarplusStrong(1);
		AudioFlow flow = synth.start();
		Thread.sleep(600000);
		flow.quit();
		synth.close();
		System.exit(0);
	}

}
