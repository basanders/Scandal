package CIS6905;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class AudioTask implements Runnable {
	
	private final ByteBuffer buffer;
	private final AudioFormat format;
	
	public AudioTask(ByteBuffer buffer, AudioFormat format) {
		this.buffer = buffer;
		this.format = format;
	}

	@Override
	public void run() {
		SourceDataLine sourceDataLine;
		try {
			sourceDataLine = AudioSystem.getSourceDataLine(format);
			sourceDataLine.open();
			sourceDataLine.start();
			sourceDataLine.write(buffer.array(), 0, buffer.position());
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
