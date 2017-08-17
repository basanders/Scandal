package framework.effects;

import framework.generators.HannWindow;
import framework.utilities.Settings;

public class Granulator {
	
	float timeScale = Settings.samplingRate * 0.001f;
	
	public float[] processVector(float[] buffer, float depth, float speed) {
		return buffer; // TODO
	}

	public float[] process(float[] buffer, float position, float deviation, float grainLength, float interGrainTime, float speed) {
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
