package framework.examples;

import framework.generators.HannWindow;

public class WindowFunctionExample {

	public static void main(String[] args) {
		new HannWindow(500).plot();
	}

}
