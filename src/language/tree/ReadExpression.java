package language.tree;

import language.compiler.Token;

public class ReadExpression extends Expression {
	
	public final Expression expression;

	public ReadExpression(Token firstToken, Expression expression) {
		super(firstToken);
		this.expression = expression;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitReadExpression(this, argument);
	}

}
