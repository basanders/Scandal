package language.sublime;

import language.compiler.Compiler;

public class Scandal {

	// TODO Array indexing
	// TODO Array operations (for loops?)
	// TODO Unary expression class (MINUS and NOT)

	public static void main(String[] args) throws Exception {
		//new Compiler().compile(args[0], args);
		Compiler compiler = new Compiler();
		byte[] bytecode = compiler.compile("doc/Filters.scandal", args);
		compiler.print(bytecode);
	}

}
