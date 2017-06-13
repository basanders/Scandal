package framework.waveforms;

public abstract class AliasedWavetable extends Wavetable {
	
	AliasedWavetable(int tableSize) {
		super(tableSize);
		fillTable();
	}

}
