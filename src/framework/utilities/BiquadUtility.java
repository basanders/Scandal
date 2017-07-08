package framework.utilities;

import framework.effects.BiquadAllPass;
import framework.effects.BiquadBandPass;
import framework.effects.BiquadHiPass;
import framework.effects.BiquadHiShelf;
import framework.effects.BiquadLowPass;
import framework.effects.BiquadLowShelf;
import framework.effects.BiquadNotch;
import framework.effects.BiquadPeak;

public class BiquadUtility {

	public float[] process(float[] buffer, float cutoff, float resonance, int method) {
		switch (method) {
		case 1: return new BiquadAllPass().process(buffer, cutoff, resonance);
		case 2: return new BiquadBandPass().process(buffer, cutoff, resonance);
		case 3: return new BiquadNotch().process(buffer, cutoff, resonance);
		case 4: return new BiquadLowPass().process(buffer, cutoff, resonance);
		case 5: return new BiquadHiPass().process(buffer, cutoff, resonance);
		case 6: return new BiquadLowShelf().process(buffer, cutoff, resonance);
		case 7: return new BiquadHiShelf().process(buffer, cutoff, resonance);
		case 8: return new BiquadPeak().process(buffer, cutoff, resonance);
		default: return buffer;
		}
	}
	
	public float[] process(float[] buffer, float[] cutoff, float resonance, int method) {
		switch (method) {
		case 1: return new BiquadAllPass().process(buffer, cutoff, resonance);
		case 2: return new BiquadBandPass().process(buffer, cutoff, resonance);
		case 3: return new BiquadNotch().process(buffer, cutoff, resonance);
		case 4: return new BiquadLowPass().process(buffer, cutoff, resonance);
		case 5: return new BiquadHiPass().process(buffer, cutoff, resonance);
		case 6: return new BiquadLowShelf().process(buffer, cutoff, resonance);
		case 7: return new BiquadHiShelf().process(buffer, cutoff, resonance);
		case 8: return new BiquadPeak().process(buffer, cutoff, resonance);
		default: return buffer;
		}
	}
	
	public float[] process(float[] buffer, float cutoff, float[] resonance, int method) {
		switch (method) {
		case 1: return new BiquadAllPass().process(buffer, cutoff, resonance);
		case 2: return new BiquadBandPass().process(buffer, cutoff, resonance);
		case 3: return new BiquadNotch().process(buffer, cutoff, resonance);
		case 4: return new BiquadLowPass().process(buffer, cutoff, resonance);
		case 5: return new BiquadHiPass().process(buffer, cutoff, resonance);
		case 6: return new BiquadLowShelf().process(buffer, cutoff, resonance);
		case 7: return new BiquadHiShelf().process(buffer, cutoff, resonance);
		case 8: return new BiquadPeak().process(buffer, cutoff, resonance);
		default: return buffer;
		}
	}
	
	public float[] process(float[] buffer, float[] cutoff, float[] resonance, int method) {
		switch (method) {
		case 1: return new BiquadAllPass().process(buffer, cutoff, resonance);
		case 2: return new BiquadBandPass().process(buffer, cutoff, resonance);
		case 3: return new BiquadNotch().process(buffer, cutoff, resonance);
		case 4: return new BiquadLowPass().process(buffer, cutoff, resonance);
		case 5: return new BiquadHiPass().process(buffer, cutoff, resonance);
		case 6: return new BiquadLowShelf().process(buffer, cutoff, resonance);
		case 7: return new BiquadHiShelf().process(buffer, cutoff, resonance);
		case 8: return new BiquadPeak().process(buffer, cutoff, resonance);
		default: return buffer;
		}
	}

}
