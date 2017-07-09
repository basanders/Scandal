package framework.utilities;

import framework.effects.RingModulator;
import framework.waveforms.AdditiveSawtooth;
import framework.waveforms.AdditiveSquare;
import framework.waveforms.AdditiveTriangle;
import framework.waveforms.WavetableCosine;
import framework.waveforms.WavetableWhite;

public class RingModulatorUtility {
	
	public float[] process(float[] buffer, float depth, float speed, int shape) {
		switch (shape) {
		case 2: return new RingModulator(new AdditiveSawtooth()).process(buffer, depth, speed);
		case 3: return new RingModulator(new AdditiveSquare()).process(buffer, depth, speed);
		case 4: return new RingModulator(new AdditiveTriangle()).process(buffer, depth, speed);
		case 5: return new RingModulator(new WavetableWhite()).process(buffer, depth, speed);
		default: return new RingModulator(new WavetableCosine()).process(buffer, depth, speed);
		}
	}
	
	public float[] process(float[] buffer, float[] depth, float speed, int shape) {
		switch (shape) {
		case 2: return new RingModulator(new AdditiveSawtooth()).process(buffer, depth, speed);
		case 3: return new RingModulator(new AdditiveSquare()).process(buffer, depth, speed);
		case 4: return new RingModulator(new AdditiveTriangle()).process(buffer, depth, speed);
		case 5: return new RingModulator(new WavetableWhite()).process(buffer, depth, speed);
		default: return new RingModulator(new WavetableCosine()).process(buffer, depth, speed);
		}
	}
	
	public float[] process(float[] buffer, float depth, float[] speed, int shape) {
		switch (shape) {
		case 2: return new RingModulator(new AdditiveSawtooth()).process(buffer, depth, speed);
		case 3: return new RingModulator(new AdditiveSquare()).process(buffer, depth, speed);
		case 4: return new RingModulator(new AdditiveTriangle()).process(buffer, depth, speed);
		case 5: return new RingModulator(new WavetableWhite()).process(buffer, depth, speed);
		default: return new RingModulator(new WavetableCosine()).process(buffer, depth, speed);
		}
	}
	
	public float[] process(float[] buffer, float[] depth, float[] speed, int shape) {
		switch (shape) {
		case 2: return new RingModulator(new AdditiveSawtooth()).process(buffer, depth, speed);
		case 3: return new RingModulator(new AdditiveSquare()).process(buffer, depth, speed);
		case 4: return new RingModulator(new AdditiveTriangle()).process(buffer, depth, speed);
		case 5: return new RingModulator(new WavetableWhite()).process(buffer, depth, speed);
		default: return new RingModulator(new WavetableCosine()).process(buffer, depth, speed);
		}
	}

}
