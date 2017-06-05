package utilities;

import java.nio.ByteBuffer;

public class StereoPingPong {
	
	public ByteBuffer stereoPingPong(double freq, int duration) {
		int samples = duration * Settings.samplingRate;
		ByteBuffer byteBuffer = ByteBuffer.allocate(samples * Settings.bitDepth / 4); // stereo
		double leftGain = 0;
		double rightGain = Short.MAX_VALUE;
		for (int i = 0; i < samples; i++) {
			if (i % (samples / 8) == 0) {
				double temp = leftGain;
				leftGain = rightGain;
				rightGain = temp;
			}
			double time = (double) i / Settings.samplingRate;
			byteBuffer.putShort((short) (leftGain * Math.sin(Math.PI * 2 * freq * time)));
			byteBuffer.putShort((short) (rightGain * Math.sin(Math.PI * 2 * freq * time)));
		}
		return byteBuffer;
	}

}
