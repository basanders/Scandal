package language.tree;

import language.compiler.Token;

public abstract class Expression extends Node {

	public Expression(Token firstToken) {
		super(firstToken);
	}

}
