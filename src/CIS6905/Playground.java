package CIS6905;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.sound.sampled.*;
import org.jfree.ui.RefineryUtilities;

public class Playground {
	
	public static final int samplingRate = 44100;
	public static final int bitDepth = 16;
	static final int vectorSize = 512;
	static final double twoPi = 2 * Math.PI;
	static final double frequencyScale = twoPi / samplingRate;
	static final double amplitudeScale = Short.MAX_VALUE;
	static final AudioFormat mono = new AudioFormat(samplingRate, bitDepth, 1, true, true);
	static final AudioFormat stereo = new AudioFormat(samplingRate, bitDepth, 2, true, true);

	public static void main(String[] args) throws Exception {
		//printDeviceList();
		//playAudioFile("/lisa.wav");
		//loopAudioFile("/lisa.wav", 1000, 6000, 16);
		//recordAudioFile("src/test", 2000);
		//playAudioFile("/test.wav");
		//ByteArrayOutputStream buffer = recordBuffer(2000);
		//playBuffer(buffer);
		//noiseGenerator(1, 0.1);
		//oscillator(2, 0.1, 440, new NaiveCosine());
		//wavetableOscillator(2, 0.1, 440, new ClassicSquare());
		//plotWavetableOscillator("Classic Square", 440, 441, new ClassicSquare());
		//plotWavetable("Blep Residual", WavetableBlep.getSharedInstance().wavetable);
	}

	static void printActiveThreads() {
		int running = 0;
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getState() == Thread.State.RUNNABLE) running++;
		}
		System.out.println("Active threads: " + running);
	}

	static void printDeviceList() {
		int index = 0;
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixer : mixers) {
			System.out.println(index++ + ": " + mixer.getName());
		}
	}

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
		Thread.sleep((int) ((end - start) * count * 1000 / samplingRate)); // samples to ms
		clip.stop();
		clip.drain();
		clip.close();
	}

	static void recordAudioFile(String name, long duration) throws Exception {
		File audioFile = new File(name + ".wav");
		TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(mono);
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

	static ByteArrayOutputStream recordBuffer(long duration) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(mono);
		targetDataLine.open();
		targetDataLine.start();
		Thread dispatch = new Thread() {
			@Override
			public void run() {
				byte[] data = new byte[vectorSize];
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
		SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(mono);
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
		Thread.sleep(buffer.size() * 500 / samplingRate);
		sourceDataLine.stop();
		sourceDataLine.drain();
		sourceDataLine.close();
	}
	
	static void noiseGenerator(int duration, double amplitude) throws Exception {
		WavetableNoise waveform = new WavetableNoise();
		int totalSamples = duration * samplingRate;
		double oscAmp = amplitude * amplitudeScale;
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		SourceDataLine line = AudioSystem.getSourceDataLine(mono);
		line.open();
		line.start();
		while (totalSamples > 0) {
			buffer.clear();
			for (int i = 0; i < vectorSize; i++) {
				buffer.putShort((short) (oscAmp * waveform.getSample()));
			}
			totalSamples -= vectorSize;
			line.write(buffer.array(), 0, buffer.position());
		}
		line.drain();
		line.stop();
		line.close();
	}

	static void oscillator(int duration, double amplitude, double frequency, NaiveWaveform waveform) throws Exception {
		int totalSamples = duration * samplingRate;
		double oscAmp = amplitude * amplitudeScale;
		double oscFreq = frequency * frequencyScale;
		double oscPhase = 0;
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		SourceDataLine line = AudioSystem.getSourceDataLine(mono);
		line.open();
		line.start();
		while (totalSamples > 0) {
			buffer.clear();
			for (int i = 0; i < vectorSize; i++) {
				buffer.putShort((short) (oscAmp * waveform.getSample(oscPhase)));
				oscPhase += oscFreq;
				if (oscPhase >= twoPi) oscPhase -= twoPi;
			}
			totalSamples -= vectorSize;
			line.write(buffer.array(), 0, buffer.position());
		}
		line.drain();
		line.stop();
		line.close();
	}
	
	// TODO This is a mess right now.
	static void wavetableOscillator(int duration, double amplitude, double frequency, Wavetable wavetable) throws Exception {
		boolean normalizedPhase = false;
		ClassicSquare table = null;
		if (wavetable instanceof ClassicSquare) {
			normalizedPhase = true;
			table = (ClassicSquare) wavetable;
		}
		int totalSamples = duration * samplingRate;
		double oscAmp = amplitude * amplitudeScale;
		double oscFreq = 0;
		if (normalizedPhase) {
			oscFreq = frequency / samplingRate;
		} else {
			oscFreq = frequency * wavetable.tableSize / samplingRate;
		}
		double oscPhase = 0;
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		SourceDataLine line = AudioSystem.getSourceDataLine(mono);
		line.open();
		line.start();
		while (totalSamples > 0) {
			buffer.clear();
			for (int i = 0; i < vectorSize; i++) {
				if (normalizedPhase) {
					buffer.putShort((short) (oscAmp * table.getSample(oscPhase, oscFreq)));
					oscPhase += oscFreq;
					if (oscPhase >= 1) oscPhase -= 1;
				} else {
					buffer.putShort((short) (oscAmp * wavetable.getSample(oscPhase)));
					oscPhase += oscFreq;
					if (oscPhase >= wavetable.tableSize) oscPhase -= wavetable.tableSize;
				}
			}
			totalSamples -= vectorSize;
			line.write(buffer.array(), 0, buffer.position());
		}
		line.drain();
		line.stop();
		line.close();
	}
	
	static void plotWavetableOscillator(String title, double frequency, int samples, ClassicSquare wavetable) {
		double[] array = new double[samples];
		double oscFreq = frequency / samplingRate;
		double oscPhase = 0;
		for (int i = 0; i < samples; i++) {
			array[i] = wavetable.getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= 1) oscPhase -= 1;
		}
		PlotUtility demo = new PlotUtility(title, array);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
	
	static void plotWavetable(String title, double[] wavetable) {
		PlotUtility demo = new PlotUtility(title, wavetable);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}
