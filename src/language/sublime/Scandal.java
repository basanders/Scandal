package language.sublime;

import language.compiler.Compiler;

public class Scandal {
	
	// TODO remove runnable's constructor in BytecodeGenerator
	// TODO reorder bytecode generation so that print statements are executed in order

	public static void main(String[] args) throws Exception {
		new Compiler().compile(args[0], args);
	}

}
