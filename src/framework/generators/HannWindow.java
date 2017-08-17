package framework.generators;

public class HannWindow extends Function {

	public HannWindow(int length) {
		super(length);
	}

	@Override
	public float[] get() {
		float[] array = new float[length];
		for (int i = 0; i < length; i++)
			array[i] = 0.5f * (1.0f - (float) Math.cos(twoPi * i / (length - 1)));
		return array;
	}

}
