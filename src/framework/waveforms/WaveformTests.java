package framework.waveforms;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WaveformTests {
	
	// TODO Test antialiased waveforms with a sweep

	private double[] naiveCases = new double[] {0, Math.PI * 0.5, Math.PI, Math.PI * 1.5, Math.PI * 2};
	private double[] aliasedCases = new double[] {0, 1024, 2048, 3072, 4095};

	@Test
	public void testNaiveCosine() {
		NaiveCosine wave = new NaiveCosine();
		checkCosineOrTriangle(wave, naiveCases, 0.0001);
	}

	@Test
	public void testWavetableCosine() {
		WavetableCosine wave = new WavetableCosine();
		checkCosineOrTriangle(wave, aliasedCases, 0.001);
	}

	@Test
	public void testNaiveSawtooth() {
		NaiveSawtooth wave = new NaiveSawtooth();
		checkSawtooth(wave, naiveCases, 0.0001);
	}

	@Test
	public void testAliasedSawtooth() {
		AliasedSawtooth wave = new AliasedSawtooth();
		checkSawtooth(wave, aliasedCases, 0.001);
	}

	@Test
	public void testAdditiveSawtooth() {
		// Offset by 186 (and 13) samples to compensate for the Gibbs effect.
		double tolerance = 0.0002;
		AdditiveSawtooth wave = new AdditiveSawtooth();
		assertEquals(wave.getSample(186, Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(1011, Double.NaN), 0.5, tolerance);
		assertEquals(wave.getSample(2048, Double.NaN), 0, tolerance);
		assertEquals(wave.getSample(3085, Double.NaN), -0.5, tolerance);
		assertEquals(wave.getSample(3910, Double.NaN), -1, tolerance);
	}

	@Test
	public void testNaiveTriangle() {
		NaiveTriangle wave = new NaiveTriangle();
		checkCosineOrTriangle(wave, naiveCases, 0.0001);
	}

	@Test
	public void testAliasedTriangle() {
		AliasedTriangle wave = new AliasedTriangle();
		checkCosineOrTriangle(wave, aliasedCases, 0.001);
	}

	@Test
	public void testAdditiveTriangle() {
		// This is a sum of cosines that converges very quickly.
		AdditiveTriangle wave = new AdditiveTriangle();
		checkCosineOrTriangle(wave, aliasedCases, 0.0001);
	}

	@Test
	public void testNaiveSquare() {
		NaiveSquare wave = new NaiveSquare();
		checkSquare(wave, naiveCases, 0.0001);
	}

	@Test
	public void testAliasedSquare() {
		AliasedSquare wave = new AliasedSquare();
		checkSquare(wave, aliasedCases, 0.001);
	}

	@Test
	public void testAdditiveSquare() {
		// Offset by 102 samples to compensate for the Gibbs effect.
		double tolerance = 0.0001;
		AdditiveSquare wave = new AdditiveSquare();
		assertEquals(wave.getSample(102, Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(1946, Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(2048, Double.NaN), 0, tolerance);
		assertEquals(wave.getSample(2150, Double.NaN), -1, tolerance);
		assertEquals(wave.getSample(3994, Double.NaN), -1, tolerance);
	}

	@Test
	public void testNaiveNoise() {
		NaiveWhite wave = new NaiveWhite();
		assertEquals(wave.getSample(Math.random() * wave.twoPi, Double.NaN), Math.random(), 2);
	}

	@Test
	public void testWavetableNoise() {
		WavetableWhite wave = new WavetableWhite();
		assertEquals(wave.getSample(Math.floor(Math.random() * wave.tableSize), Double.NaN), Math.random(), 2);
	}

	private void checkCosineOrTriangle(Waveform wave, double[] cases, double tolerance) {
		assertEquals(wave.getSample(cases[0], Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[1], Double.NaN), 0, tolerance);
		assertEquals(wave.getSample(cases[2], Double.NaN), -1, tolerance);
		assertEquals(wave.getSample(cases[3], Double.NaN), 0, tolerance);
		assertEquals(wave.getSample(cases[4], Double.NaN), 1, tolerance);
	}

	private void checkSawtooth(Waveform wave, double[] cases, double tolerance) {
		assertEquals(wave.getSample(cases[0], Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[1], Double.NaN), 0.5, tolerance);
		assertEquals(wave.getSample(cases[2], Double.NaN), 0, tolerance);
		assertEquals(wave.getSample(cases[3], Double.NaN), -0.5, tolerance);
		assertEquals(wave.getSample(cases[4], Double.NaN), -1, tolerance);
	}

	private void checkSquare(Waveform wave, double[] cases, double tolerance) {
		assertEquals(wave.getSample(cases[0], Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[1], Double.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[2], Double.NaN), -1, tolerance);
		assertEquals(wave.getSample(cases[3], Double.NaN), -1, tolerance);
		assertEquals(wave.getSample(cases[4], Double.NaN), -1, tolerance);
	}

}
