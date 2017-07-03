package language.tree;

import language.compiler.Token;

public class SpeedExpression extends Expression {
	
	public final Expression speed;

	public SpeedExpression(Token firstToken, Expression speed) {
		super(firstToken);
		this.speed = speed;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return null;
	}

}
