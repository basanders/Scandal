package utilities;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public final class Settings {

	public static final int samplingRate = 44100;
	public static final int bitDepth = 16;
	public static final int vectorSize = 1024;
	public static final AudioFormat mono = new AudioFormat(samplingRate, bitDepth, 1, true, true);
	public static final AudioFormat stereo = new AudioFormat(samplingRate, bitDepth, 2, true, true);
	private static Settings sharedInstance;

	private Settings() {}

	public static Settings getSharedInstance() {
		if (sharedInstance == null) sharedInstance = new Settings();
		return sharedInstance;
	}
	
	public static void printInfo() {
		System.out.println(
				"Sampling rate: " + samplingRate + "\n" +
				"Bit depth: " + bitDepth + "\n" +
				"Vector size: " + vectorSize);
	}

	public static void printDeviceList() {
		int index = 0;
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixer : mixers) {
			System.out.println(index++ + ": " + mixer.getName());
		}
	}

	public static void printMidiDeviceList() {
		int index = 0;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info : infos) {
			System.out.println(index++ + ": " + info.getName());
		}
	}

}
