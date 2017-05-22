package CIS6905;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import CIS6905.waveforms.AdditiveTriangle;
import CIS6905.waveforms.ClassicSawtooth;
import CIS6905.waveforms.ClassicSquare;
import CIS6905.waveforms.Wavetable;
import CIS6905.waveforms.WavetableCosine;

public class WavetableOscillator {

	private final Wave wave;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	public enum Wave {
		COSINE(new WavetableCosine()),
		SAWTOOTH(new ClassicSawtooth()),
		TRIANGLE(new AdditiveTriangle()),
		SQUARE(new ClassicSquare());
		
		Wavetable wavetable;
		
		private Wave(Wavetable wavetable) {
			this.wavetable = wavetable;
		}
	}
	
	public WavetableOscillator(Wave wave) {
		this.wave = wave;
	}
	
	private double runningPhase = 0;
	
	public ByteBuffer getVector(double amplitude, double frequency) {
		int samples = Settings.vectorSize;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = frequency * wave.wavetable.tableSize / Settings.samplingRate;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(runningPhase, oscFreq)));
			runningPhase += oscFreq;
			if (runningPhase >= wave.wavetable.tableSize) runningPhase -= wave.wavetable.tableSize;
		}
		return buffer;
	}
	
	public void start(int delay, int duration, double amplitude, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = frequency * wave.wavetable.tableSize / Settings.samplingRate;
		double oscPhase = 0;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(oscPhase, oscFreq)));
			oscPhase += oscFreq;
			if (oscPhase >= wave.wavetable.tableSize) oscPhase -= wave.wavetable.tableSize;
		}
		//scheduler.schedule(new AudioTask(buffer, Settings.mono), delay, MILLISECONDS);
		scheduler.submit(new AudioTask(buffer, Settings.mono));
	}
	
	public void start(int delay, int duration, double[] envelope, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = 0;
		double oscFreq = frequency * wave.wavetable.tableSize / Settings.samplingRate;
		double oscPhase = 0;
		double envelopeIndex = 0;
		double envelopeIncrement = (double) envelope.length / samples;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			oscAmp = envelope[(int) envelopeIndex] * Short.MAX_VALUE; // TODO make this logarithmic
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(oscPhase, oscFreq)));
			envelopeIndex += envelopeIncrement;
			oscPhase += oscFreq;
			if (oscPhase >= wave.wavetable.tableSize) oscPhase -= wave.wavetable.tableSize;
		}
		//scheduler.schedule(new AudioTask(buffer, Settings.mono), delay, MILLISECONDS);
		scheduler.submit(new AudioTask(buffer, Settings.mono));
	}
	
	public void start(int delay, int duration, double amplitude, double[] glide) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = 0;
		double oscPhase = 0;
		double glideIndex = 0;
		double glideIncrement = (double) glide.length / samples;
		double frequencyScale = (double) wave.wavetable.tableSize / Settings.samplingRate;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			oscFreq = glide[(int) glideIndex] * frequencyScale; // TODO make this logarithmic
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(oscPhase, oscFreq)));
			glideIndex += glideIncrement;
			oscPhase += oscFreq;
			if (oscPhase >= wave.wavetable.tableSize) oscPhase -= wave.wavetable.tableSize;
		}
		//scheduler.schedule(new AudioTask(buffer, Settings.mono), delay, MILLISECONDS);
		scheduler.submit(new AudioTask(buffer, Settings.mono));
	}
	
	public void start(int delay, int duration, double[] envelope, double[] glide) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = 0;
		double oscFreq = 0;
		double oscPhase = 0;
		double envelopeIndex = 0;
		double envelopeIncrement = (double) envelope.length / samples;
		double glideIndex = 0;
		double glideIncrement = (double) glide.length / samples;
		double frequencyScale = (double) wave.wavetable.tableSize / Settings.samplingRate;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			oscAmp = envelope[(int) envelopeIndex] * Short.MAX_VALUE; // TODO make this logarithmic
			oscFreq = glide[(int) glideIndex] * frequencyScale; // TODO make this logarithmic
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(oscPhase, oscFreq)));
			envelopeIndex += envelopeIncrement;
			glideIndex += glideIncrement;
			oscPhase += oscFreq;
			if (oscPhase >= wave.wavetable.tableSize) oscPhase -= wave.wavetable.tableSize;
		}
		//scheduler.schedule(new AudioTask(buffer, Settings.mono), delay, MILLISECONDS);
		scheduler.submit(new AudioTask(buffer, Settings.mono));
	}

}
