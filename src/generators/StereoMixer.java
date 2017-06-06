package generators;

import java.util.Arrays;

public class StereoMixer {
	
	public double[] render(AudioTrack... tracks) {
		int duration = 0;
		for (AudioTrack track : tracks) if (track.end >= duration) duration = track.end;
		double[] mixdown = new double[duration];
		Arrays.fill(mixdown, 0);
		double[] vector;
		for (AudioTrack track : tracks) {
			vector = track.getVector();
			for (int i = track.start; i < track.end; i++) {
				mixdown[i] += vector[i - track.start];
			}
		}
		return mixdown;
	}

}
