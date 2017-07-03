package language.tree;

import language.compiler.Token;

public class ReverseExpression extends Expression {
	
	public final Expression array;

	public ReverseExpression(Token firstToken, Expression array) {
		super(firstToken);
		this.array = array;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitReverseExpression(this, argument);
	}

}
