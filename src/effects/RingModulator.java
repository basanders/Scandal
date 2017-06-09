package effects;

import utilities.Settings;
import waveforms.Wavetable;

public class RingModulator implements EffectsProcessor {
	
	private Wavetable table;
	public double runningPhase = 0;
	
	public RingModulator(Wavetable table) {
		this.table = table;
	}
	
	public double[] processVector(double[] buffer, double depth, double speed) {
		int samples = buffer.length;
		double freq = speed * table.tableSize / Settings.samplingRate;
		double[] processedBuffer = new double[buffer.length];
		double tableSample = 0;
		for (int i = 0; i < samples; i++) {
			tableSample = (table.getSample(runningPhase, freq) + 1) * 0.5;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1); 
			runningPhase += freq;
			if (runningPhase >= table.tableSize) runningPhase -= table.tableSize;
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double depth, double speed) {
		int samples = buffer.length;
		double freq = speed * table.tableSize / Settings.samplingRate;
		double[] processedBuffer = new double[buffer.length];
		double phase = 0;
		double tableSample = 0;
		for (int i = 0; i < samples; i++) {
			tableSample = (table.getSample(phase, freq) + 1) * 0.5;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1); 
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;
	}
	
	public double[] process(double[] buffer, double[] depths, double speed) {
		int samples = buffer.length;
		double freq = speed * table.tableSize / Settings.samplingRate;
		double[] processedBuffer = new double[buffer.length];
		double phase = 0;
		double tableSample = 0;
		double depthIndex = 0;
		double depthIncrement = (double) depths.length / samples;
		double depth = 0;
		for (int i = 0; i < samples; i++) {
			depth = depths[(int) depthIndex];
			tableSample = (table.getSample(phase, freq) + 1) * 0.5;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1);
			depthIndex += depthIncrement;
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;
	}
	
	public double[] process(double[] buffer, double depth, double[] speeds) {
		int samples = buffer.length;
		double[] processedBuffer = new double[buffer.length];
		double phase = 0;
		double freq = 0;
		double freqIndex = 0;
		double freqIncrement = (double) speeds.length / samples;
		double freqScale = (double) table.tableSize / Settings.samplingRate;
		double tableSample = 0;
		for (int i = 0; i < samples; i++) {
			freq = speeds[(int) freqIndex] * freqScale;
			tableSample = (table.getSample(phase, freq) + 1) * 0.5;
			processedBuffer[i] = buffer[i] * (tableSample * depth - depth + 1);
			freqIndex += freqIncrement;
			phase += freq;
			if (phase >= table.tableSize) phase -= table.tableSize;
		}
		return processedBuffer;
	}
	
	public double[] process(double[] buffer, double[] depths, double[] speeds) {
		int samples = buffer.length;
		double depth = 0;
		double depthIndex = 0;
		double depthIncrement = (double) depths.length / samples;
		double freq = 0;
		double freqIndex = 0;
		double freqIncrement = (double) speeds.length / samples;
		double freqScale = (double) table.tableSize / Settings.samplingRate;
		double phase = 0;
		double[] processedBuffer = new double[buffer.length];
		double tableSample = 0;
		for (int i = 0; i < samples; i++) {
			depth = depths[(int) depthIndex];
			tableSample = (table.getSample(phase, freq) + 1) * 0.5;
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
