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
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

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
	
	public void start(int delay, int duration, double amplitude, double frequency) {
		switch (wave) {
		case SAWTOOTH:
		case SQUARE: {
			scheduler.schedule(new AudioTask(getNormalized(duration, amplitude, frequency), Settings.mono), delay, MILLISECONDS);
		} break;
		default: {
			scheduler.schedule(new AudioTask(get(duration, amplitude, frequency), Settings.mono), delay, MILLISECONDS);
		} break;
		}
	}
	
	public void start(int duration, double amplitude, double initialFrequency, double finalFrequency) {
		switch (wave) {
		case SAWTOOTH:
		case SQUARE: {
			new AudioThread(getNormalized(duration, amplitude, initialFrequency), Settings.mono).start();
		} break;
		default: {
			new AudioThread(get(duration, amplitude, initialFrequency, finalFrequency), Settings.mono).start();
		} break;
		}
	}
	
	private ByteBuffer get(int duration, double amplitude, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = frequency * wave.wavetable.tableSize / Settings.samplingRate;
		double oscPhase = 0;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(oscPhase)));
			oscPhase += oscFreq;
			if (oscPhase >= wave.wavetable.tableSize) oscPhase -= wave.wavetable.tableSize;
		}
		return buffer;
	}
	
	private ByteBuffer get(int duration, double amplitude, double initialFrequency, double finalFrequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double frequencyScale = (double) wave.wavetable.tableSize / Settings.samplingRate;
		double oscPhase = 0;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			buffer.putShort((short) (oscAmp * wave.wavetable.getSample(oscPhase)));
			double frequency = initialFrequency + (double) i * (finalFrequency - initialFrequency) / samples;
			frequency *= frequencyScale;
			oscPhase += frequency;
			if (oscPhase >= wave.wavetable.tableSize) oscPhase -= wave.wavetable.tableSize;
		}
		return buffer;
	}
	
	private ByteBuffer getNormalized(int duration, double amplitude, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = frequency / Settings.samplingRate;
		double oscPhase = 0;
		double sample = 0;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // mono
		for (int i = 0; i < samples; i++) {
			if (wave == Wave.SAWTOOTH) sample = ((ClassicSawtooth) wave.wavetable).getSample(oscPhase, oscFreq);
			if (wave == Wave.SQUARE) sample = ((ClassicSquare) wave.wavetable).getSample(oscPhase, oscFreq);
			buffer.putShort((short) (oscAmp * sample));
			oscPhase += oscFreq;
			if (oscPhase >= 1) oscPhase -= 1;
		}
		return buffer;
	}
	
	ByteBuffer getNormalized(int duration, double amplitude, double initialFrequency, double finalFrequency) {
		// TODO
		return null;
	}

}
