package framework.effects;

import java.util.ArrayList;

import framework.generators.HannWindow;
import framework.utilities.Settings;

public class Granulator {
	
	private final float timeScale = Settings.samplingRate * 0.001f;
	public int playbackPosition = 4410;
	public int playbackDeviation = 441;
	public float playbackSpeed = 1;
	private int grainLength = 4410;
	public int interGrainTime = 44;
	private final int grainCount = 256;
	private final int windowSize = 8192;
	private double windowIncrement = (double) windowSize / grainLength;
	private final float[] window = new HannWindow(windowSize).get();
	private final float[] captureBuffer = new float[11025];
	private int captureBufferIndex = 0;
	private final ArrayList<Grain> grainArray = new ArrayList<>();
	private int igtCounter = 0;
	private int grainArrayCounter = 0;
	
	class Grain {
		
		boolean isBusy;
		private float index;
		private float sample;
		private double windowIndex;
		
		Grain() {
			reset();
		}
		
		void reset() {
			isBusy = false;
			index = playbackPosition + (((float) Math.random() * 2 - 1) * playbackDeviation);
			windowIndex = 0;
		}
		
		float getSample() {
			sample = captureBuffer[(int) index] * window[(int) windowIndex] * 0.1f;
			index += playbackSpeed;
			if (index >= captureBuffer.length) index -= captureBuffer.length;
			windowIndex += windowIncrement;
			if (windowIndex >= windowSize) reset();
			return sample;
		}
		
	}
	
	public Granulator() {
		for (int i = 0; i < grainCount; i++) grainArray.add(i, new Grain());
		grainArray.get(0).isBusy = true;
	}
	
	public void setGrainLength(int grainLength) {
		this.grainLength = grainLength;
		windowIncrement = (double) windowSize / grainLength;
	}
	
	private void updateGrainArray() {
		igtCounter++;
		if (igtCounter >= interGrainTime) {
			igtCounter -= interGrainTime;
			grainArray.get(grainArrayCounter).isBusy = true;
			grainArrayCounter++;
			if (grainArrayCounter >= grainCount) grainArrayCounter = 0;
		}
	}
	
	public float[] processVector(float[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			captureBuffer[captureBufferIndex++] = buffer[i];
			if (captureBufferIndex >= captureBuffer.length) captureBufferIndex = 0; 
			for (Grain grain : grainArray) if (grain.isBusy) buffer[i] += grain.getSample();
			updateGrainArray();
		}
		return buffer;
	}

	public float[] process(float[] buffer, float position, float deviation, float speed, float grainLength, float interGrainTime) {
		if (position + deviation + grainLength >= buffer.length) return buffer;
		if (position - deviation < 0) return buffer;
		if (interGrainTime <= 0) return buffer;
		position *= timeScale;
		deviation *= timeScale;
		grainLength *= timeScale;
		interGrainTime *= timeScale;
		float[] processedBuffer = new float[buffer.length];
		float grainCount = (buffer.length - grainLength) / interGrainTime;
		float bufferIndex;
		float grainIndex;
		float[] window = new HannWindow((int) grainLength).get();
		for (int i = 0; i < grainCount; i++) {
			bufferIndex = i * interGrainTime;
			grainIndex = position + (((float) Math.random() * 2 - 1) * deviation);
			for (int j = 0; j < (int) grainLength; j++) {
				processedBuffer[(int) bufferIndex++] += buffer[(int) grainIndex] * window[j];
				grainIndex += speed;
				if (grainIndex >= buffer.length)
					grainIndex = position + (((float) Math.random() * 2 - 1) * deviation);
			}
		}
		return processedBuffer;
	}

}
