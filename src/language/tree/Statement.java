package language.tree;

import language.compiler.Token;

public abstract class Statement extends Node {
	
	public final Expression expression;
	
	public Statement(Token firstToken, Expression expression) {
		super(firstToken);
		this.expression = expression;
	}

}
