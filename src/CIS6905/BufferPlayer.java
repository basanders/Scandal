package CIS6905;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BufferPlayer {
	
	private static ScheduledExecutorService scheduler;
	
	public BufferPlayer(int voices) {
		BufferPlayer.scheduler = Executors.newScheduledThreadPool(voices);
		// dummy task to force instantiation
		scheduler.schedule(new AudioTask(ByteBuffer.allocate(0), Settings.mono), 0, MILLISECONDS);
	}
	
	public void playMono(int delay, double[] doubles) {
		ByteBuffer buffer = ByteBuffer.allocate(doubles.length * Settings.bitDepth / 8);
		for (int i = 0; i < doubles.length; i++) {
			buffer.putShort((short) (doubles[i] * Short.MAX_VALUE));
		}
		scheduler.schedule(new AudioTask(buffer, Settings.mono), delay, MILLISECONDS);		
	}
	
	public void playStereo(int delay, ByteBuffer buffer) {
		scheduler.schedule(new AudioTask(buffer, Settings.stereo), delay, MILLISECONDS);
	}
	
	public void stop() {
		// needs to be called if number of voices is greater than zero
		scheduler.shutdown();
	}

}
