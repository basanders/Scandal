package framework.waveforms;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WaveformTests {
	
	// TODO Test antialiased waveforms with a sweep

	private float[] naiveCases = new float[]
			{0, (float) Math.PI * 0.5f, (float) Math.PI, (float) Math.PI * 1.5f, (float) Math.PI * 2};
	private float[] aliasedCases = new float[] {0, 1024, 2048, 3072, 4095};

	@Test
	public void testNaiveCosine() {
		NaiveCosine wave = new NaiveCosine();
		checkCosineOrTriangle(wave, naiveCases, 0.0001f);
	}

	@Test
	public void testWavetableCosine() {
		WavetableCosine wave = new WavetableCosine();
		checkCosineOrTriangle(wave, aliasedCases, 0.001f);
	}

	@Test
	public void testNaiveSawtooth() {
		NaiveSawtooth wave = new NaiveSawtooth();
		checkSawtooth(wave, naiveCases, 0.0001f);
	}

	@Test
	public void testAliasedSawtooth() {
		AliasedSawtooth wave = new AliasedSawtooth();
		checkSawtooth(wave, aliasedCases, 0.001f);
	}

	@Test
	public void testAdditiveSawtooth() {
		// Offset by 186 (and 13) samples to compensate for the Gibbs effect.
		float tolerance = 0.0002f;
		AdditiveSawtooth wave = new AdditiveSawtooth();
		assertEquals(wave.getSample(186, Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(1011, Float.NaN), 0.5, tolerance);
		assertEquals(wave.getSample(2048, Float.NaN), 0, tolerance);
		assertEquals(wave.getSample(3085, Float.NaN), -0.5, tolerance);
		assertEquals(wave.getSample(3910, Float.NaN), -1, tolerance);
	}

	@Test
	public void testNaiveTriangle() {
		NaiveTriangle wave = new NaiveTriangle();
		checkCosineOrTriangle(wave, naiveCases, 0.0001f);
	}

	@Test
	public void testAliasedTriangle() {
		AliasedTriangle wave = new AliasedTriangle();
		checkCosineOrTriangle(wave, aliasedCases, 0.001f);
	}

	@Test
	public void testAdditiveTriangle() {
		// This is a sum of cosines that converges very quickly.
		AdditiveTriangle wave = new AdditiveTriangle();
		checkCosineOrTriangle(wave, aliasedCases, 0.0001f);
	}

	@Test
	public void testNaiveSquare() {
		NaiveSquare wave = new NaiveSquare();
		checkSquare(wave, naiveCases, 0.0001f);
	}

	@Test
	public void testAliasedSquare() {
		AliasedSquare wave = new AliasedSquare();
		checkSquare(wave, aliasedCases, 0.001f);
	}

	@Test
	public void testAdditiveSquare() {
		// Offset by 102 samples to compensate for the Gibbs effect.
		float tolerance = 0.0001f;
		AdditiveSquare wave = new AdditiveSquare();
		assertEquals(wave.getSample(102, Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(1946, Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(2048, Float.NaN), 0, tolerance);
		assertEquals(wave.getSample(2150, Float.NaN), -1, tolerance);
		assertEquals(wave.getSample(3994, Float.NaN), -1, tolerance);
	}

	@Test
	public void testNaiveNoise() {
		NaiveWhite wave = new NaiveWhite();
		assertEquals(wave.getSample((float) Math.random() * wave.twoPi, Float.NaN), Math.random(), 2);
	}

	@Test
	public void testWavetableNoise() {
		WavetableWhite wave = new WavetableWhite();
		assertEquals(wave.getSample((float) Math.floor(Math.random() * wave.tableSize), Float.NaN), Math.random(), 2);
	}

	private void checkCosineOrTriangle(Waveform wave, float[] cases, float tolerance) {
		assertEquals(wave.getSample(cases[0], Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[1], Float.NaN), 0, tolerance);
		assertEquals(wave.getSample(cases[2], Float.NaN), -1, tolerance);
		assertEquals(wave.getSample(cases[3], Float.NaN), 0, tolerance);
		assertEquals(wave.getSample(cases[4], Float.NaN), 1, tolerance);
	}

	private void checkSawtooth(Waveform wave, float[] cases, float tolerance) {
		assertEquals(wave.getSample(cases[0], Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[1], Float.NaN), 0.5, tolerance);
		assertEquals(wave.getSample(cases[2], Float.NaN), 0, tolerance);
		assertEquals(wave.getSample(cases[3], Float.NaN), -0.5, tolerance);
		assertEquals(wave.getSample(cases[4], Float.NaN), -1, tolerance);
	}

	private void checkSquare(Waveform wave, float[] cases, float tolerance) {
		assertEquals(wave.getSample(cases[0], Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[1], Float.NaN), 1, tolerance);
		assertEquals(wave.getSample(cases[2], Float.NaN), -1, tolerance);
		assertEquals(wave.getSample(cases[3], Float.NaN), -1, tolerance);
		assertEquals(wave.getSample(cases[4], Float.NaN), -1, tolerance);
	}

}
