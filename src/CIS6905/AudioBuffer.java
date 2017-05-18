package CIS6905;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioBuffer {
	
	static ByteArrayOutputStream recordBuffer(long duration) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(Settings.mono);
		targetDataLine.open();
		targetDataLine.start();
		Thread dispatch = new Thread() {
			@Override
			public void run() {
				byte[] data = new byte[Settings.vectorSize];
				int bytes = 1;
				while (bytes != 0) {
					bytes = targetDataLine.read(data, 0, data.length); // read from target
					buffer.write(data, 0, bytes); // write to buffer
				}
			}
		};
		dispatch.start();
		Thread.sleep(duration);
		targetDataLine.stop();
		targetDataLine.drain();
		targetDataLine.close();
		return buffer;
	}

	static void playBuffer(ByteArrayOutputStream buffer) throws Exception {
		SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(Settings.mono);
		sourceDataLine.open();
		sourceDataLine.start();
		Thread dispatch = new Thread() {
			@Override
			public void run() {
				int bytes = 1;
				while (bytes != 0) {
					bytes = sourceDataLine.write(buffer.toByteArray(), 0, buffer.size());
				}
			}
		};
		dispatch.start();
		// Divide by two because 16-bit audio takes two bytes per sample.
		Thread.sleep(buffer.size() * 500 / Settings.samplingRate);
		sourceDataLine.stop();
		sourceDataLine.drain();
		sourceDataLine.close();
	}

}
