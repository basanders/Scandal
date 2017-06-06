package generators;

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

import utilities.Settings;

public class AudioTask {

	private static ScheduledExecutorService scheduler;

	public AudioTask() {
		new AudioTask(0);
	}
	
	public AudioTask(int voices) {
		AudioTask.scheduler = Executors.newScheduledThreadPool(voices);
		// dummy task to force instantiation
		scheduler.schedule(() -> null, 0, MILLISECONDS);
	}

	public void playMono(int delay, double[] doubles) {
		playInterleaved(delay, doubles, Settings.mono);
	}

	public void playStereo(int delay, double[] doubles) {
		playInterleaved(delay, doubles, Settings.stereo);
	}

	private void playInterleaved(int delay, double[] doubles, AudioFormat format) {
		ByteBuffer buffer = ByteBuffer.allocate(doubles.length * Settings.bitDepth / 8);
		for (int i = 0; i < doubles.length; i++) {
			buffer.putShort((short) (doubles[i] * Short.MAX_VALUE));
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

	public double[] record(int duration) throws Exception {
		return scheduler.submit(() -> this.recordTask(duration)).get();
	}

	private double[] recordTask(int duration) throws Exception {
		TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(Settings.mono);
		targetDataLine.open();
		targetDataLine.start();
		int samples = duration * Settings.samplingRate / 1000;
		double[] buffer = new double[samples];
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
	
	public void exportStereo(String name, double[] doubles) throws Exception {
		File file = new File("wav", name + ".wav");
		ByteBuffer buffer = ByteBuffer.allocate(doubles.length * Settings.bitDepth / 8);
		for (int i = 0; i < doubles.length; i++) {
			buffer.putShort((short) (doubles[i] * Short.MAX_VALUE));
		}
		InputStream is = new ByteArrayInputStream(buffer.array());
		AudioInputStream ais = new AudioInputStream(is, Settings.stereo, doubles.length / 2);
		int result = AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		System.out.println(result + " bytes written to " + file.getName());
	}

}
