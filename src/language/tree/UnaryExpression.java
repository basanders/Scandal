package language.tree;

import language.compiler.Token;

public class UnaryExpression extends Expression {
	
	public final Expression e;

	public UnaryExpression(Token firstToken, Expression expression) {
		super(firstToken);
		this.e = expression;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitUnaryExpression(this, arg);
	}

}
