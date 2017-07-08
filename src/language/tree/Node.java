package language.tree;

import language.compiler.Token;

public abstract class Node {
	
	public static enum Type { INT, FLOAT, BOOL, STRING, ARRAY, FORMAT, FILTER, WAVEFORM }

	public final Token firstToken;
	public Type type;
	public String jvmType;

	public Node(Token firstToken) {
		this.firstToken = firstToken;
		this.type = getType();
	}
	
	public Type getType() {
		switch (firstToken.kind) {
		case KW_INT: return Type.INT;
		case KW_FLOAT: return Type.FLOAT;
		case KW_BOOL: return Type.BOOL;
		case KW_STRING: return Type.STRING;
		case KW_ARRAY: return Type.ARRAY;
		case KW_FORMAT: return Type.FORMAT;
		case KW_FILTER: return Type.FILTER;
		case KW_WAVEFORM: return Type.WAVEFORM;
		default: return null;
		}
	}
	
	public String getJvmType() {
		switch (firstToken.kind) {
		case KW_INT: return "I";
		case KW_FLOAT: return "F";
		case KW_BOOL: return "Z";
		case KW_STRING: return "Ljava/lang/String;";
		case KW_ARRAY: return "[F";
		case KW_FORMAT: return "I";
		case KW_FILTER: return "I";
		case KW_WAVEFORM: return "I";
		default: return null;
		}
	}

	public abstract Object visit(NodeVisitor visitor, Object arg) throws Exception;

}
