package utilities;

import generators.AudioFlow;
import generators.KarplusStrong;

public class Playground {

	public static void main(String[] args) throws Exception {
		KarplusStrong synth = new KarplusStrong();
		AudioFlow flow = synth.start();
		Thread.sleep(10000);
		flow.quit();
		synth.close();
		System.exit(0);
	}

}
