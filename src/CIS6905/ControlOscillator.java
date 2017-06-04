package CIS6905;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import CIS6905.utilities.Settings;
import CIS6905.waveforms.NaiveWaveform;

public class ControlOscillator {
	
	void naiveOscillator(int duration, int amplitude, int frequency, NaiveWaveform waveform) throws Exception {
		int totalSamples = duration * Settings.samplingRate;
		double oscAmp = amplitude * Short.MAX_VALUE;
		double oscFreq = frequency * waveform.twoPi / Settings.samplingRate;
		double oscPhase = 0;
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		SourceDataLine line = AudioSystem.getSourceDataLine(Settings.mono);
		line.open();
		line.start();
		while (totalSamples > 0) {
			buffer.clear();
			for (int i = 0; i < Settings.vectorSize; i++) {
				buffer.putShort((short) (oscAmp * waveform.getSample(oscPhase, oscFreq)));
				oscPhase += oscFreq;
				if (oscPhase >= waveform.twoPi) oscPhase -= waveform.twoPi;
			}
			totalSamples -= Settings.vectorSize;
			line.write(buffer.array(), 0, buffer.position());
		}
		line.drain();
		line.stop();
		line.close();
	}

}
