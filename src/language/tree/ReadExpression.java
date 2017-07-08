package language.tree;

import language.compiler.Token;

public class ReadExpression extends Expression {
	
	public final Expression fileName;
	public final Expression format;

	public ReadExpression(Token firstToken, Expression fileName, Expression format) {
		super(firstToken);
		this.fileName = fileName;
		this.format = format;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitReadExpression(this, argument);
	}

}
