package CIS6905;

import static CIS6905.WavetableOscillator.Wave.*;

public class Playground {

	public static void main(String[] args) throws Exception {
		/*playAudioFile("/lisa.wav");
		loopAudioFile("/lisa.wav", 1000, 6000, 16);
		recordAudioFile("src/test", 2000);
		playAudioFile("/test.wav");
		ByteArrayOutputStream buffer = recordBuffer(2000);
		playBuffer(buffer);
		noiseGenerator(1, 0.1);
		oscillator(2, 0.1, 440, new NaiveCosine());
		wavetableOscillator(2, 0.1, 1024, new ClassicSquare());
		new ClassicSquare().plot(2, 441);
		new AudioThread(oscillator(440, 2), mono).start();
		new AudioThread(sweep(0, 22050, 5), mono).start();
		new AudioThread(decayPulse(880, 5), mono).start();
		new AudioThread(stereoPanning(880, 2), stereo).start();
		new AudioThread(stereoPingPong(880, 5), stereo).start();*/
		
		BreakpointFunction envelope = new BreakpointFunction(512, new double[]{220, 660, 440, 1100});
		
		WavetableOscillator sine = new WavetableOscillator(COSINE);
		WavetableOscillator saw = new WavetableOscillator(SAWTOOTH);
		WavetableOscillator square = new WavetableOscillator(SQUARE);
		
		for (int i = 0; i < 200; i++) {
			sine.start((2 * i) * 60, 40, envelope.getArray(), Math.abs((i + 2) * 440 / 2 * Math.pow(-1, i)) % 2000);
			saw.start((2 * i + 1) * 60, 30, envelope.getArray(), Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
			square.start((2 * i + 2) * 60, 20, envelope.getArray(), Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
		}
		
	}
	
}
