package framework.generators;

import java.nio.ByteBuffer;

public interface RealTimePerformer {
	
	public abstract ByteBuffer getVector();
	
	public abstract AudioFlow start();

}
