package framework.examples;

import framework.generators.AudioFlow;
import framework.utilities.Settings;

public class AudioFlowRecordExample {

	public static void main(String[] args) throws Exception {
		AudioFlow flow = new AudioFlow("doc/test.wav", Settings.mono);
		Thread.sleep(2000);
		flow.quit();
	}

}
