package language.tree;

import language.compiler.Token;

public class Declaration extends Node {
	
	public final Token identToken;
	
	public Declaration(Token firstToken, Token identToken) {
		super(firstToken);
		this.identToken = identToken;
	}

}
