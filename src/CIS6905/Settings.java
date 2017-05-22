package CIS6905;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public final class Settings {
	
	public static final int samplingRate = 44100;
	public static final int bitDepth = 16;
	public static final int vectorSize = 2048;
	public static final AudioFormat mono = new AudioFormat(samplingRate, bitDepth, 1, true, true);
	public static final AudioFormat stereo = new AudioFormat(samplingRate, bitDepth, 2, true, true);
	private static Settings sharedInstance;
	
	private Settings() {}
	
	public static Settings getSharedInstance() {
		if (sharedInstance == null) sharedInstance = new Settings();
		return sharedInstance;
	}
	
	public static void printDeviceList() {
		int index = 0;
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixer : mixers) {
			System.out.println(index++ + ": " + mixer.getName());
		}
	}

}
