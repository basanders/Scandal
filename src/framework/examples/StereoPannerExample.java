package framework.examples;

import framework.generators.AudioTask;
import framework.generators.StereoPanner;
import framework.generators.WaveFile;
import framework.waveforms.NaiveSquare;

public class StereoPannerExample {

	public static void main(String[] args) throws Exception {
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		float[] pingPong = new NaiveSquare().getTable(512, 4);
		float[] stereo = new StereoPanner().process(lisa, pingPong);
		new AudioTask().playStereo(stereo);
	}

}
