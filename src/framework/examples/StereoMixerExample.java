package framework.examples;

import framework.generators.AudioTask;
import framework.generators.AudioTrack;
import framework.generators.StereoMixer;
import framework.generators.WaveFile;
import framework.generators.WavetableOscillator;
import framework.waveforms.ClassicSawtooth;

public class StereoMixerExample {

	public static void main(String[] args) throws Exception {
		float[] saw = new WavetableOscillator(new ClassicSawtooth()).get(4000, 0.7f, 880);
		float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
		AudioTrack sawTrack = new AudioTrack(saw, 3000, 0.2f, -0.8f);
		AudioTrack lisaTrack = new AudioTrack(lisa, 0, 1, 0.8f);
		float[] mixdown = new StereoMixer().render(sawTrack, lisaTrack);
		new AudioTask().playStereo(mixdown);
		new AudioTask().export(mixdown, "mix.wav", 2);
	}

}
