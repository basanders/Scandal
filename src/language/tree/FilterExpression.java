package language.tree;

import language.compiler.Token;

public class FilterExpression extends Expression {
	
	public final int method;

	public FilterExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.FILTER;
		this.method = getMethod(firstToken);
	}
	
	private int getMethod(Token token) {
		switch (token.kind) {
		case KW_ALLPASS: return 1;
		case KW_BANDPASS: return 2;
		case KW_BANDSTOP: return 3;
		case KW_LOWPASS: return 4;
		case KW_HIPASS: return 5;
		case KW_LOWSHELF: return 6;
		case KW_HISHELF: return 7;
		case KW_PEAKING: return 8;
		default: return 0;
		}
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitFilterExpression(this, arg);
	}

}
