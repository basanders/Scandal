package framework.utilities;

import language.compiler.Compiler;

public class Playground {

	public static void main(String[] args) throws Exception {
		/*KarplusStrong synth = new KarplusStrong(1);
		AudioFlow flow = synth.start();
		Thread.sleep(600000);
		flow.quit();
		synth.close();
		System.exit(0);*/
		String code = "bool test = true bool result int p = 51 int q = 2 "
				+ "while (q < p) { if (p % q == 0) { test = false q = p - 1 } q = q + 1 } result = test";
		new Compiler().compileAndRun("PrimeFinder", code, args);
	}

}
