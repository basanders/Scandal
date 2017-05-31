package CIS6905;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class WaveFile {
	
	public final Path path;
	private final int formatChunkSize;
	public final int numberOfChannels;
	public final int samplingRate;
	public final int bitDepth;
	private final int dataStartIndex;
	private final int dataLength;
	private final double[] interleavedBuffer;
	
	public WaveFile(String name) throws Exception {
		this.path = FileSystems.getDefault().getPath("wav", name);
		byte[] byteArray = Files.readAllBytes(path);
		this.formatChunkSize =
				(byteArray[16] & 0xff) |
				(byteArray[17] & 0xff) << 8 |
				(byteArray[18] & 0xff) << 16 |
				(byteArray[19]) << 24;
		this.numberOfChannels = (byteArray[22] & 0xff) | (byteArray[23]) << 8;
		if (numberOfChannels > 2) throw new Exception("Only stereo and mono files are supported.");
		this.samplingRate =
				(byteArray[24] & 0xff) |
				(byteArray[25] & 0xff) << 8 |
				(byteArray[26] & 0xff) << 16 |
				(byteArray[27]) << 24;
		this.bitDepth = (byteArray[34] & 0xff) | (byteArray[35]) << 8;
		if (bitDepth != 16) throw new Exception("Only 16-bit files are supported.");
		this.dataStartIndex = 28 + formatChunkSize;
		this.dataLength =
				(byteArray[dataStartIndex - 4] & 0xff) |
				(byteArray[dataStartIndex - 3] & 0xff) << 8 |
				(byteArray[dataStartIndex - 2] & 0xff) << 16 |
				(byteArray[dataStartIndex - 1]) << 24;
		// fill interleaved buffer
		interleavedBuffer = new double[dataLength / 2];
		for (int i = 0, j = dataStartIndex; j < dataLength; i++, j += 2) {
			interleavedBuffer[i] = (byteArray[j] & 0xff) | (byteArray[j + 1] << 8);
			interleavedBuffer[i] /= Short.MAX_VALUE;
		}
	}
	
	public double[] getLeftChannel() {
		if (numberOfChannels == 1) return interleavedBuffer;
		double[] left = new double[interleavedBuffer.length / 2];
		for (int i = 0, j = 0; i < interleavedBuffer.length; i += 2, j++) {
			left[j] = interleavedBuffer[i];
		}
		return left;
	}
	
	public double[] getRightChannel() {
		if (numberOfChannels == 1) return interleavedBuffer;
		double[] right = new double[interleavedBuffer.length / 2];
		for (int i = 1, j = 0; i < interleavedBuffer.length - 1; i += 2, j++) {
			right[j] = interleavedBuffer[i];
		}
		return right;
	}
	
	public double[] getMonoSum() {
		if (numberOfChannels == 1) return interleavedBuffer;
		double[] sum = new double[interleavedBuffer.length / 2];
		for (int i = 1, j = 0; i < interleavedBuffer.length - 1; i += 2, j++) {
			sum[j] = (interleavedBuffer[i] + interleavedBuffer[i - 1]) * 0.5;
		}
		return sum;
	}
	
	public void printInfo() {
		System.out.println(
				"File name: " + path.getFileName() + "\n" +
				"Channel count: " + numberOfChannels + "\n" +
				"Sampling rate: " + samplingRate + "\n" +
				"Bit depth: " + bitDepth);
	}

}
