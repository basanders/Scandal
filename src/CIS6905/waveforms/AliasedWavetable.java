package CIS6905.waveforms;

public abstract class AliasedWavetable extends Wavetable {
	
	AliasedWavetable(int tableSize) {
		super(tableSize);
		fillTable();
	}

}
