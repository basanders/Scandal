package framework.generators;

import framework.effects.Gain;
import framework.utilities.Settings;

public class AudioTrack {

	public int start;
	public int end;
	private float[] vector;

	public AudioTrack(float[] buffer, int start, float gain, float pan) {
		this.start = start * (Settings.samplingRate / 500);
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gain, pan);
	}

	public AudioTrack(float[] buffer, int start, float[] gains, float pan) {
		this.start = start * Settings.samplingRate / 500;
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gains, pan);
	}

	public AudioTrack(float[] buffer, int start, float gain, float[] pans) {
		this.start = start * Settings.samplingRate / 500;
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gain, pans);
	}

	public AudioTrack(float[] buffer, int start, float[] gains, float[] pans) {
		this.start = start * Settings.samplingRate / 500;
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gains, pans);
	}

	public float[] getVector() {
		return vector;
	}
	
	public float[] getShiftedVector() {
		float[] buffer = new float[end];
		for (int i = 0; i < start; i++) buffer[i] = 0;
		for (int i = start; i < end; i++) buffer[i] = vector[i - start];
		return buffer;
	}

	private void fillVector(float[] buffer, float gain, float pan) {
		vector = new StereoPanner().process(new Gain().process(buffer, gain), pan);
	}

	private void fillVector(float[] buffer, float[] gains, float pan) {
		vector = new StereoPanner().process(new Gain().process(buffer, gains), pan);
	}

	private void fillVector(float[] buffer, float gain, float[] pans) {
		vector = new StereoPanner().process(new Gain().process(buffer, gain), pans);
	}

	private void fillVector(float[] buffer, float[] gains, float[] pans) {
		vector = new StereoPanner().process(new Gain().process(buffer, gains), pans);
	}

}
