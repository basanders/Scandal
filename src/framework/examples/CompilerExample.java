package framework.examples;

import language.compiler.Compiler;

public class CompilerExample {
	
	// TODO Array indexing
	// TODO Array operations (for loops?)

	public static void main(String[] args) throws Exception {
		Compiler compiler = new Compiler();
		byte[] bytecode = compiler.compile("doc/Screenshot.scandal", args);
		compiler.print(bytecode);		
		compiler.save("bin/Screenshot.class", bytecode);
	}

}
