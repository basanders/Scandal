package language.tree;

import language.compiler.Token;

public abstract class Expression extends Node {

	public Expression(Token firstToken) {
		super(firstToken);
	}
	
	@Override
	public String getJvmType() {
		switch (this.type) {
		case INT: return "I";
		case FLOAT: return "F";
		case BOOL: return "Z";
		case STRING: return "Ljava/lang/String;";
		case ARRAY: return "[F";
		case FORMAT: return "I";
		case FILTER: return "I";
		default: return null;
		}
	}

}
