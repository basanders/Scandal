package framework.generators;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import framework.utilities.Settings;

public class AudioTask {

	private static ScheduledExecutorService scheduler;

	public AudioTask() {
		AudioTask.scheduler = Executors.newScheduledThreadPool(0);
		// dummy task to force instantiation
		scheduler.schedule(() -> null, 0, MILLISECONDS);
	}

	public AudioTask(int voices) {
		AudioTask.scheduler = Executors.newScheduledThreadPool(voices);
		scheduler.schedule(() -> null, 0, MILLISECONDS);
	}
	
	public void play(float[] floats, int channels) {
		if (channels == 1) playInterleaved(0, floats, Settings.mono);
		else if (channels == 2) playInterleaved(0, floats, Settings.stereo);
	}

	public void playMono(float[] floats) {
		playInterleaved(0, floats, Settings.mono);
	}

	public void playMono(int delay, float[] floats) {
		playInterleaved(delay, floats, Settings.mono);
	}

	public void playStereo(float[] floats) {
		playInterleaved(0, floats, Settings.stereo);
	}

	public void playStereo(int delay, float[] floats) {
		playInterleaved(delay, floats, Settings.stereo);
	}

	private void playInterleaved(int delay, float[] floats, AudioFormat format) {
		ByteBuffer buffer = ByteBuffer.allocate(floats.length * Settings.bitDepth / 8);
		for (int i = 0; i < floats.length; i++) {
			buffer.putShort((short) (floats[i] * Short.MAX_VALUE));
		}
		scheduler.schedule(() -> {
			try {
				this.playTask(buffer, format);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, delay, MILLISECONDS);
	}

	private void playTask(ByteBuffer buffer, AudioFormat format) throws Exception {
		SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);
		sourceDataLine.open();
		sourceDataLine.start();
		sourceDataLine.write(buffer.array(), 0, buffer.position());
		sourceDataLine.drain();
		sourceDataLine.stop();
		sourceDataLine.close();
	}

	public void stop() {
		// needs to be called if number of voices is greater than zero
		scheduler.shutdown();
	}

	public float[] record(int duration) throws Exception {
		return scheduler.submit(() -> this.recordTask(duration)).get();
	}

	private float[] recordTask(int duration) throws Exception {
		TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(Settings.mono);
		targetDataLine.open();
		targetDataLine.start();
		int samples = duration * Settings.samplingRate / 1000;
		float[] buffer = new float[samples];
		int vectorBytes = Settings.vectorSize * Settings.bitDepth / 8;
		byte[] data = new byte[vectorBytes];
		int offset = 0;
		while (offset <= samples - Settings.vectorSize) {
			targetDataLine.read(data, 0, vectorBytes); // read from target
			for (int i = 0, j = 0; j < vectorBytes; i++, j += 2) {
				// This is big endian.
				buffer[i + offset] = (data[j + 1] & 0xff) | data[j] << 8;
				buffer[i + offset] /= Short.MAX_VALUE;
			}
			offset += Settings.vectorSize;
		}
		targetDataLine.drain();
		targetDataLine.stop();
		targetDataLine.close();
		return buffer;
	}

	public void export(float[] floats, String name, int channels) throws Exception {
		File file = new File(name);
		ByteBuffer buffer = ByteBuffer.allocate(floats.length * Settings.bitDepth / 8);
		for (int i = 0; i < floats.length; i++) {
			buffer.putShort((short) (floats[i] * Short.MAX_VALUE));
		}
		InputStream is = new ByteArrayInputStream(buffer.array());
		AudioFormat format = channels == 1 ? Settings.mono : Settings.stereo;
		AudioInputStream ais = new AudioInputStream(is, format, floats.length / channels);
		int result = AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		System.out.println(result + " bytes written to " + file.getName());
	}

}
