package language.sublime;

import language.compiler.Compiler;

public class Main {
	
	// TODO Array type
	// TODO Unary expression class (MINUS and NOT)

	public static void main(String[] args) throws Exception {
		new Compiler().compile(args[0], args);
	}

}
