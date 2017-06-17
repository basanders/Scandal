package language.examples;

import language.compiler.Compiler;

public class Playground {

	public static void main(String[] args) throws Exception {
		Compiler compiler = new Compiler();
		byte[] bytecode = compiler.compile("NaivePrimeFinder.scandal", args);
		compiler.print(bytecode);
	}

}
