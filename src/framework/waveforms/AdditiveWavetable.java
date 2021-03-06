package framework.waveforms;

public abstract class AdditiveWavetable extends Wavetable {
	
	int harmonicCount;
	
	AdditiveWavetable(int tableSize, int harmonicCount) {
		super(tableSize);
		this.harmonicCount = harmonicCount;
		fillTable();
	}

}
