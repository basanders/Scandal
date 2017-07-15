package framework.examples;

import framework.generators.AudioTask;

public class AudioTaskRecordExample {

	public static void main(String[] args) throws Exception {
		AudioTask task = new AudioTask();
		float[] buffer = task.record(2000);
		int channels = 1;
		task.export(buffer, "doc/test.wav", channels);
		task.playMono(buffer);
	}

}
