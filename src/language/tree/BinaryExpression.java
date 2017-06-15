package language.tree;

import language.compiler.Token;

public class BinaryExpression extends Expression {

	public final Expression e0;
	public final Token operator;
	public final Expression e1;

	public BinaryExpression(Token firstToken, Expression e0, Token operator, Expression e1) {
		super(firstToken);
		this.e0 = e0;
		this.operator = operator;
		this.e1 = e1;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitBinaryExpression(this, argument);
	}

}
