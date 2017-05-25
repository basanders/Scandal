package CIS6905;

import java.nio.ByteBuffer;

public abstract class RealTimePerformer {
	
	double runningPhase = 0;
	
	public abstract ByteBuffer getVector(double amplitude, double frequency);

}
