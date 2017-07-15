package framework.examples;

import framework.effects.BiquadPeak;
import framework.generators.BreakpointFunction;
import framework.generators.WaveFile;
import framework.utilities.Settings;
import framework.waveforms.AdditiveSquare;
import framework.waveforms.WavetableResidual;

public class UtilitiesExample {

	public static void main(String[] args) throws Exception {
		Settings.printInfo();
		new WaveFile("doc/monoLisa.wav").printInfo();
		new AdditiveSquare().plot(512, 2);
		WavetableResidual.getSharedInstance().plot(500, 1);
		new BreakpointFunction(512, new float[]{0, 0.5f, 0, 1, 0, 1, 0, 0.5f, 0}).plot();
		new BiquadPeak().plotMagnitudeResponse(1000, 100, 3);
		new WaveFile("doc/monoLisa.wav").plot(1000);
	}

}
