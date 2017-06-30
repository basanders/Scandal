package framework.effects;

import framework.utilities.Settings;
import framework.waveforms.Wavetable;

public class RingModulator implements EffectsProcessor {
	
	private Wavetable table;
	public float runningPhase = 0;
	
	public RingModulator(Wavetable table) {
		this.table = table;
	}
	
	public float[] processVector(float[] buffer, float depth, float speed) {
		int samples = buffer.length;
		float freq = speed * table.tableSize / Settings.samplingRate;
		float[] processedBuffer = new float[buffer.length];
		float tableSample = 0;
		for (int i = 0; i < samples; i++) {
			tableSample = (table.getSample(runningPhase, freq) + 1) * 0.5f;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1); 
			runningPhase += freq;
			if (runningPhase >= table.tableSize) runningPhase -= table.tableSize;
		}
		return processedBuffer;
	}

	public float[] process(float[] buffer, float depth, float speed) {
		int samples = buffer.length;
		float freq = speed * table.tableSize / Settings.samplingRate;
		float[] processedBuffer = new float[buffer.length];
		float phase = 0;
		float tableSample = 0;
		for (int i = 0; i < samples; i++) {
			tableSample = (table.getSample(phase, freq) + 1) * 0.5f;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1); 
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;
	}
	
	public float[] process(float[] buffer, float[] depths, float speed) {
		int samples = buffer.length;
		float freq = speed * table.tableSize / Settings.samplingRate;
		float[] processedBuffer = new float[buffer.length];
		float phase = 0;
		float tableSample = 0;
		float depthIndex = 0;
		float depthIncrement = (float) depths.length / samples;
		float depth = 0;
		for (int i = 0; i < samples; i++) {
			depth = depths[(int) depthIndex];
			tableSample = (table.getSample(phase, freq) + 1) * 0.5f;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1);
			depthIndex += depthIncrement;
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;
	}
	
	public float[] process(float[] buffer, float depth, float[] speeds) {
		int samples = buffer.length;
		float[] processedBuffer = new float[buffer.length];
		float phase = 0;
		float freq = 0;
		float freqIndex = 0;
		float freqIncrement = (float) speeds.length / samples;
		float freqScale = (float) table.tableSize / Settings.samplingRate;
		float tableSample = 0;
		for (int i = 0; i < samples; i++) {
			freq = speeds[(int) freqIndex] * freqScale;
			tableSample = (table.getSample(phase, freq) + 1) * 0.5f;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1);
			freqIndex += freqIncrement;
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;
	}
	
	public float[] process(float[] buffer, float[] depths, float[] speeds) {
		int samples = buffer.length;
		float depth = 0;
		float depthIndex = 0;
		float depthIncrement = (float) depths.length / samples;
		float freq = 0;
		float freqIndex = 0;
		float freqIncrement = (float) speeds.length / samples;
		float freqScale = (float) table.tableSize / Settings.samplingRate;
		float phase = 0;
		float[] processedBuffer = new float[buffer.length];
		float tableSample = 0;
		for (int i = 0; i < samples; i++) {
			depth = depths[(int) depthIndex];
			tableSample = (table.getSample(phase, freq) + 1) * 0.5f;
			freq = speeds[(int) freqIndex] * freqScale;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1); 
			depthIndex += depthIncrement;
			freqIndex += freqIncrement;
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;		
	}

}
