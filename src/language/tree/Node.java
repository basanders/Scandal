package language.tree;

import language.compiler.Token;

public abstract class Node {
	
	public final Token firstToken;
	
	public Node(Token firstToken) {
		this.firstToken = firstToken;
	}

}
