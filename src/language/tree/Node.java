package language.tree;

import language.compiler.Token;

public abstract class Node {
	
	public static enum Type { INT, FLOAT, BOOL }

	public final Token firstToken;
	public Type type;

	public Node(Token firstToken) {
		this.firstToken = firstToken;
	}
	
	public static Type getType(Token token) {
		switch (token.kind) {
		case INT_LIT: return Type.INT;
		case FLOAT_LIT: return Type.FLOAT;
		case KW_BOOL: return Type.BOOL;
		default: return null;
		}
	}

	public abstract Object visit(NodeVisitor visitor, Object argument) throws Exception;

}
