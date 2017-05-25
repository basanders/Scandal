package CIS6905;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;

public class AudioFlow implements Runnable, LineListener {
	
	//private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);
	private final RealTimePerformer performer;
	private final AudioFormat format;
	private boolean running = true;
	private double amplitude;
	private double frequency;
	
	public AudioFlow(RealTimePerformer performer, AudioFormat format) {
		this.performer = performer;
		this.format = format;
	}
	
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	
	public void quit() {
		this.running = false;
	}
	
	@Override
	public void update(LineEvent event) {
		//System.out.println(event.getType());
	}

	@Override
	public void run() {
		try {
			SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);
			//sourceDataLine.addLineListener(this);
			sourceDataLine.open(format, Settings.vectorSize * 2); // 16-bit equals 2 bytes per sample
			sourceDataLine.start();
			//long startTime = 0;
			while (running) {
				ByteBuffer buffer = performer.getVector(amplitude, frequency);
				//ByteBuffer buffer = scheduler.submit(() -> performer.getVector(amplitude, frequency)).get();
				//startTime = System.nanoTime();
				sourceDataLine.write(buffer.array(), 0, buffer.position());
				//System.out.println((System.nanoTime() - startTime) / 1000000);
			}
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
