package CIS6905;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.TargetDataLine;

public class AudioFile {
	
	static void playAudioFile(String path) throws Exception {
		InputStream file = Playground.class.getResourceAsStream(path);
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		clip.start();
		Thread.sleep(clip.getFrameLength());
		clip.stop();
		clip.drain();
		clip.close();
	}

	static void loopAudioFile(String path, double start, double end, double count) throws Exception {
		InputStream file = Playground.class.getResourceAsStream(path);
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		clip.setLoopPoints((int) start, (int) end);
		clip.loop((int) count);
		Thread.sleep((int) ((end - start) * count * 1000 / Settings.samplingRate)); // samples to ms
		clip.stop();
		clip.drain();
		clip.close();
	}

	static void recordAudioFile(String name, long duration) throws Exception {
		File audioFile = new File(name + ".wav");
		TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(Settings.mono);
		AudioInputStream ais = new AudioInputStream(targetDataLine);
		targetDataLine.open();
		targetDataLine.start();
		// The recording task should be dispatched to a new thread while the main thread sleeps.
		Thread dispatch = new Thread() {
			@Override
			public void run() {
				try {
					AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		dispatch.start();
		Thread.sleep(duration);
		targetDataLine.stop();
		targetDataLine.drain();
		targetDataLine.close();
	}

}
