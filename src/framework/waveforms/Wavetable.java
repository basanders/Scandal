package framework.waveforms;

public abstract class Wavetable extends Waveform {
	
	public final int tableSize;
	float[] wavetable;
	
	Wavetable(int size) {
		super(false);
		tableSize = size;
		wavetable = new float[tableSize];
	}

	public abstract void fillTable();
	
	@Override
	public float[] getTable(int samples, float frequency) {
		float[] array = new float[samples];
		float oscFreq = tableSize * frequency / samples;
		float oscPhase = 0;
		for (int i = 0; i < samples; i++) {
			array[i] = getSample(oscPhase, oscFreq);
			oscPhase += oscFreq;
			if (oscPhase >= tableSize) oscPhase -= tableSize;
		}
		return array;
	}
	
}
