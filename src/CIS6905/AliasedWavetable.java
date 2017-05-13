package CIS6905;

public abstract class AliasedWavetable extends Wavetable {
	
	AliasedWavetable(int tableSize) {
		super(tableSize);
		fillTable();
	}

}
