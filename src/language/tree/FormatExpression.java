package language.tree;

import language.compiler.Token;
import language.compiler.Token.Kind;

public class FormatExpression extends Expression {
	
	public int channels;

	public FormatExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.FORMAT;
		if (firstToken.kind == Kind.KW_MONO) this.channels = 1;
		else if (firstToken.kind == Kind.KW_STEREO) this.channels = 2;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitFormatExpression(this, argument);
	}

}
