package framework.generators;

import framework.effects.Gain;
import framework.utilities.Settings;

public class AudioTrack {

	public int start;
	public int end;
	private double[] vector;

	public AudioTrack(double[] buffer, int start, double gain, double pan) {
		this.start = start * (Settings.samplingRate / 500);
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gain, pan);
	}

	public AudioTrack(double[] buffer, int start, double[] gains, double pan) {
		this.start = start * Settings.samplingRate / 500;
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gains, pan);
	}

	public AudioTrack(double[] buffer, int start, double gain, double[] pans) {
		this.start = start * Settings.samplingRate / 500;
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gain, pans);
	}

	public AudioTrack(double[] buffer, int start, double[] gains, double[] pans) {
		this.start = start * Settings.samplingRate / 500;
		this.end = 2 * buffer.length + this.start;
		fillVector(buffer, gains, pans);
	}

	public double[] getVector() {
		return vector;
	}

	private void fillVector(double[] buffer, double gain, double pan) {
		vector = new StereoPanner().process(new Gain().process(buffer, gain), pan);
	}

	private void fillVector(double[] buffer, double[] gains, double pan) {
		vector = new StereoPanner().process(new Gain().process(buffer, gains), pan);
	}

	private void fillVector(double[] buffer, double gain, double[] pans) {
		vector = new StereoPanner().process(new Gain().process(buffer, gain), pans);
	}

	private void fillVector(double[] buffer, double[] gains, double[] pans) {
		vector = new StereoPanner().process(new Gain().process(buffer, gains), pans);
	}

}
