package CIS6905.generators;

import java.nio.ByteBuffer;

public abstract class RealTimePerformer {
	
	double runningPhase = 0;
	
	public abstract ByteBuffer getVector();

}
