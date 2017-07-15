package language.sublime;

import language.compiler.Compiler;

public class Scandal {

	public static void main(String[] args) throws Exception {
		new Compiler().compile(args[0], args);
	}

}
