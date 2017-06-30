package language.tree;

import language.compiler.Token;

public abstract class Node {
	
	public static enum Type { INT, FLOAT, BOOL, STRING }

	public final Token firstToken;
	public Type type;
	public String jvmType;

	public Node(Token firstToken) {
		this.firstToken = firstToken;		
	}
	
	public Type getType() {
		switch (firstToken.kind) {
		case KW_INT: return Type.INT;
		case KW_FLOAT: return Type.FLOAT;
		case KW_BOOL: return Type.BOOL;
		case KW_STRING: return Type.STRING;
		default: return null;
		}
	}
	
	public String getJvmType() {
		switch (firstToken.kind) {
		case KW_INT: return "I";
		case KW_FLOAT: return "F";
		case KW_BOOL: return "Z";
		case KW_STRING: return "Ljava/lang/String;";
		default: return null;
		}
	}

	public abstract Object visit(NodeVisitor visitor, Object argument) throws Exception;

}
