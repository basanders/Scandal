package framework.effects;

import framework.utilities.Settings;

public class Delay implements EffectsProcessor {

	public double[] process(double[] buffer, double time, double feedback) {
		return process(buffer, time, feedback, 0.5);
	}

	public double[] process(double[] buffer, double time, double feedback, double mix) {
		double samples = time * (double) Settings.samplingRate / 1000; // ms to samples
		if (samples <= 0 || samples >= buffer.length) return buffer; // sanity check
		double[] processedBuffer = new double[buffer.length];
		double[] circularBuffer = new double[buffer.length];
		int readIndex = 0;
		int writeIndex = (int) samples;
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = mix * circularBuffer[readIndex] + (1 - mix) * buffer[i];
			circularBuffer[writeIndex] = buffer[i] + circularBuffer[readIndex] * feedback;
			readIndex++;
			if (readIndex >= circularBuffer.length) readIndex -= circularBuffer.length;
			writeIndex++;
			if (writeIndex >= circularBuffer.length) writeIndex -= circularBuffer.length;
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double time, double[] feedbacks, double mix) {
		double samples = time * (double) Settings.samplingRate / 1000;
		if (samples <= 0 || samples >= buffer.length) return buffer;
		double[] processedBuffer = new double[buffer.length];
		double[] circularBuffer = new double[buffer.length];
		int readIndex = 0;
		int writeIndex = (int) samples;
		double feedbackIndex = 0;
		double feedbackIncrement = (double) feedbacks.length / buffer.length;
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = mix * circularBuffer[readIndex] + (1 - mix) * buffer[i];
			circularBuffer[writeIndex] = buffer[i] + circularBuffer[readIndex] * feedbacks[(int) feedbackIndex];
			feedbackIndex += feedbackIncrement;
			readIndex++;
			if (readIndex >= circularBuffer.length) readIndex -= circularBuffer.length;
			writeIndex++;
			if (writeIndex >= circularBuffer.length) writeIndex -= circularBuffer.length;
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double time, double feedback, double[] mixes) {
		double samples = time * (double) Settings.samplingRate / 1000; // ms to samples
		if (samples <= 0 || samples >= buffer.length) return buffer; // sanity check
		double[] processedBuffer = new double[buffer.length];
		double[] circularBuffer = new double[buffer.length];
		int readIndex = 0;
		int writeIndex = (int) samples;
		double mixIndex = 0;
		double mixIncrement = (double) mixes.length / buffer.length;
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = mixes[(int) mixIndex] * circularBuffer[readIndex] + (1 - mixes[(int) mixIndex]) * buffer[i];
			circularBuffer[writeIndex] = buffer[i] + circularBuffer[readIndex] * feedback;
			mixIndex += mixIncrement;
			readIndex++;
			if (readIndex >= circularBuffer.length) readIndex -= circularBuffer.length;
			writeIndex++;
			if (writeIndex >= circularBuffer.length) writeIndex -= circularBuffer.length;
		}
		return processedBuffer;
	}

	public double[] process(double[] buffer, double time, double[] feedbacks, double[] mixes) {
		double samples = time * (double) Settings.samplingRate / 1000; // ms to samples
		if (samples <= 0 || samples >= buffer.length) return buffer; // sanity check
		double[] processedBuffer = new double[buffer.length];
		double[] circularBuffer = new double[buffer.length];
		int readIndex = 0;
		int writeIndex = (int) samples;
		double feedbackIndex = 0;
		double feedbackIncrement = (double) feedbacks.length / buffer.length;
		double mixIndex = 0;
		double mixIncrement = (double) mixes.length / buffer.length;
		for (int i = 0; i < buffer.length; i++) {
			processedBuffer[i] = mixes[(int) mixIndex] * circularBuffer[readIndex] + (1 - mixes[(int) mixIndex]) * buffer[i];
			circularBuffer[writeIndex] = buffer[i] + circularBuffer[readIndex] * feedbacks[(int) feedbackIndex];
			feedbackIndex += feedbackIncrement;
			mixIndex += mixIncrement;
			readIndex++;
			if (readIndex >= circularBuffer.length) readIndex -= circularBuffer.length;
			writeIndex++;
			if (writeIndex >= circularBuffer.length) writeIndex -= circularBuffer.length;
		}
		return processedBuffer;
	}

}
