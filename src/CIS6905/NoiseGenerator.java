package CIS6905;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import CIS6905.waveforms.WavetableWhite;

public class NoiseGenerator {
	
	static void noiseGenerator(int duration, double amplitude) throws Exception {
		WavetableWhite waveform = new WavetableWhite();
		int totalSamples = duration * Settings.samplingRate;
		double oscAmp = amplitude * Short.MAX_VALUE;
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		SourceDataLine line = AudioSystem.getSourceDataLine(Settings.mono);
		line.open();
		line.start();
		while (totalSamples > 0) {
			buffer.clear();
			for (int i = 0; i < Settings.vectorSize; i++) {
				buffer.putShort((short) (oscAmp * waveform.getSample()));
			}
			totalSamples -= Settings.vectorSize;
			line.write(buffer.array(), 0, buffer.position());
		}
		line.drain();
		line.stop();
		line.close();
	}

}
