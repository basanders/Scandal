package CIS6905.generators;

import java.nio.ByteBuffer;

import CIS6905.utilities.Settings;
import CIS6905.waveforms.Wavetable;

public class WavetableOscillator extends RealTimePerformer {

	private final Wavetable wavetable;
	private double amplitude;
	private double frequency;
	
	public WavetableOscillator(Wavetable wavetable) {
		this.wavetable = wavetable;
	}
	
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	
	public AudioFlow start() {
		AudioFlow flow = new AudioFlow(this, Settings.mono);
		new Thread(flow).start();
		return flow;
	}
	
	public ByteBuffer getVector() {
		int samples = Settings.vectorSize;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = frequency * wavetable.tableSize / Settings.samplingRate;
		ByteBuffer buffer = ByteBuffer.allocate(samples * Settings.bitDepth / 8); // 16-bit mono
		for (int i = 0; i < samples; i++) {
			buffer.putShort((short) (oscAmp * wavetable.getSample(runningPhase, oscFreq)));
			runningPhase += oscFreq;
			if (runningPhase >= wavetable.tableSize) runningPhase -= wavetable.tableSize;
		}
		return buffer;
	}
	
	public double[] get(int duration, double amplitude, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscFreq = frequency * wavetable.tableSize / Settings.samplingRate;
		double oscPhase = 0;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			buffer[i] = amplitude * wavetable.getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= wavetable.tableSize) oscPhase -= wavetable.tableSize;
		}
		return buffer;
	}
	
	public double[] get(int duration, double[] envelope, double frequency) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = 0;
		double oscFreq = frequency * wavetable.tableSize / Settings.samplingRate;
		double oscPhase = 0;
		double envelopeIndex = 0;
		double envelopeIncrement = (double) envelope.length / samples;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			oscAmp = envelope[(int) envelopeIndex];
			buffer[i] = oscAmp * wavetable.getSample(oscPhase, oscFreq);
			envelopeIndex += envelopeIncrement;
			oscPhase += oscFreq;
			if (oscPhase >= wavetable.tableSize) oscPhase -= wavetable.tableSize;
		}
		return buffer;
	}
	
	public double[] get(int duration, double amplitude, double[] glide) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscFreq = 0;
		double oscPhase = 0;
		double glideIndex = 0;
		double glideIncrement = (double) glide.length / samples;
		double frequencyScale = (double) wavetable.tableSize / Settings.samplingRate;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			oscFreq = glide[(int) glideIndex] * frequencyScale;
			buffer[i] = amplitude * wavetable.getSample(oscPhase, oscFreq);
			glideIndex += glideIncrement;
			oscPhase += oscFreq;
			if (oscPhase >= wavetable.tableSize) oscPhase -= wavetable.tableSize;
		}
		return buffer;
	}
	
	public double[] get(int duration, double[] envelope, double[] glide) {
		int samples = duration * Settings.samplingRate / 1000;
		double oscAmp = 0;
		double oscFreq = 0;
		double oscPhase = 0;
		double envelopeIndex = 0;
		double envelopeIncrement = (double) envelope.length / samples;
		double glideIndex = 0;
		double glideIncrement = (double) glide.length / samples;
		double frequencyScale = (double) wavetable.tableSize / Settings.samplingRate;
		double[] buffer = new double[samples];
		for (int i = 0; i < samples; i++) {
			oscAmp = envelope[(int) envelopeIndex];
			oscFreq = glide[(int) glideIndex] * frequencyScale;
			buffer[i] = oscAmp * wavetable.getSample(oscPhase, oscFreq);
			envelopeIndex += envelopeIncrement;
			glideIndex += glideIncrement;
			oscPhase += oscFreq;
			if (oscPhase >= wavetable.tableSize) oscPhase -= wavetable.tableSize;
		}
		return buffer;
	}

}
