package CIS6905;

import javax.sound.sampled.AudioFormat;

public class Settings {
	
	public static final int samplingRate = 44100;
	public static final int bitDepth = 16;
	public static final int vectorSize = 512;
	public static final AudioFormat mono = new AudioFormat(samplingRate, bitDepth, 1, true, true);
	public static final AudioFormat stereo = new AudioFormat(samplingRate, bitDepth, 2, true, true);
	private static Settings sharedInstance;
	
	private Settings() {}
	
	public static Settings getSharedInstance() {
		if (sharedInstance == null) sharedInstance = new Settings();
		return sharedInstance;
	}

}
