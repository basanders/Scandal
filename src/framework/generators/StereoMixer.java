package framework.generators;

import java.util.Arrays;

public class StereoMixer {
	
	public float[] render(AudioTrack... tracks) {
		int duration = 0;
		for (AudioTrack track : tracks) if (track.end >= duration) duration = track.end;
		float[] mixdown = new float[duration];
		Arrays.fill(mixdown, 0);
		float[] vector;
		for (AudioTrack track : tracks) {
			vector = track.getVector();
			for (int i = track.start; i < track.end; i++) {
				mixdown[i] += vector[i - track.start];
			}
		}
		return mixdown;
	}
	
	public float[] render(float[]... buffers) {
		int duration = 0;
		for (float[] buffer : buffers) if (buffer.length >= duration) duration = buffer.length;
		float[] mixdown = new float[duration];
		for (float[] buffer : buffers) {
			for (int i = 0; i < buffer.length; i++) {
				mixdown[i] += buffer[i];
			}
		}
		return mixdown;
	}

}
