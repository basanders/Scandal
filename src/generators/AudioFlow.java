package generators;

import java.io.File;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import utilities.Settings;

public class AudioFlow implements Runnable, LineListener {

	private final RealTimePerformer performer;
	private final AudioFormat format;
	private final String name;
	private final boolean record;
	private boolean running = true;
	private TargetDataLine targetDataLine;

	public AudioFlow(RealTimePerformer performer, AudioFormat format) {
		this.performer = performer;
		this.format = format;
		this.name = null;
		this.record = false;
	}

	public AudioFlow(String name, AudioFormat format) {
		this.performer = null;
		this.format = format;
		this.name = name;
		this.record = true;
		new Thread(this).start();
	}

	public void quit() {
		if (record) {
			targetDataLine.drain();
			targetDataLine.stop();
			targetDataLine.close();
		} else {
			this.running = false;
		}
	}

	@Override
	public void update(LineEvent event) {
		System.out.println("Audio flow event: " + event.getType());
	}

	@Override
	public void run() {
		try {
			if (record) {
				record(name);
			} else {
				play();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void play() throws Exception {
		SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);
		sourceDataLine.addLineListener(this);
		sourceDataLine.open(format, Settings.vectorSize * Settings.bitDepth / 8);
		sourceDataLine.start();
		ByteBuffer buffer;
		while (running) {
			buffer = performer.getVector();
			sourceDataLine.write(buffer.array(), 0, buffer.position());
		}
		sourceDataLine.drain();
		sourceDataLine.stop();
		sourceDataLine.close();
	}

	private void record(String name) throws Exception {
		File file = new File("wav", name + ".wav");
		targetDataLine = AudioSystem.getTargetDataLine(Settings.mono);
		targetDataLine.addLineListener(this);
		AudioInputStream ais = new AudioInputStream(targetDataLine);
		targetDataLine.open();
		targetDataLine.start();
		while (targetDataLine.isOpen()) {
			int result = AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
			System.out.println(result + " bytes written to " + file.getName());
		}
	}

}
