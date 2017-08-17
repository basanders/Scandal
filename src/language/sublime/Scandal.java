package language.sublime;

import language.compiler.Compiler;

public class Scandal {
	
	// TODO remove runnable's constructor (and main?) in BytecodeGenerator
	// TODO reorder bytecode generation so that print statements are executed in order
	// TODO change to double all counters to increase precision
	// TODO Add Granulator class to the language
	// TODO Add HannWindow class to the language

	public static void main(String[] args) throws Exception {
		new Compiler().compile(args[0], args);
	}

}
