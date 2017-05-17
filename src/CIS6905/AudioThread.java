package CIS6905;

import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class AudioThread extends Thread {
		
	private ByteBuffer byteBuffer;
	private AudioFormat format;
	
	public AudioThread(ByteBuffer byteBuffer, AudioFormat format) {
		this.byteBuffer = byteBuffer;
		this.format = format;
	}
		
	@Override
	public void run() {
		try{
			SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);
			sourceDataLine.open();
			sourceDataLine.start();
			sourceDataLine.write(byteBuffer.array(), 0, byteBuffer.position());
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}