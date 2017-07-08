package language.tree.function;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;

public class FormatExpression extends Expression {
	
	public final int channels;

	public FormatExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.FORMAT;
		this.channels = getChannels(firstToken);
	}
	
	private int getChannels(Token token) {
		switch (token.kind) {
		case KW_MONO: return 1;
		case KW_STEREO: return 2;
		default: return 0;
		}
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitFormatExpression(this, argument);
	}

}
