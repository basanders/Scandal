package framework.utilities;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public final class Settings {

	public static final int midiController = 1;
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
		System.out.println(getInfo());
	}
	
	public static String getInfo() {
		String output = "Sampling rate: " + samplingRate + "\n";
		output += "Bit depth: " + bitDepth + "\n";
		output += "Vector size: " + vectorSize + "\n";
		output += "MIDI controller: " + midiController + "\n";
		int index = 0;
		output += "Audio devices:\n";
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixer : mixers) {
			output += index++ + ": " + mixer.getName() + "\n";
		}
		index = 0;
		output += "MIDI devices:\n";
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info : infos) {
			output += index++ + ": " + info.getName() + "\n";
		}
		return output;
	}

}
