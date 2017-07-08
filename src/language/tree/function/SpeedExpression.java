package language.tree.function;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;

public class SpeedExpression extends Expression {
	
	public final Expression array;
	public final Expression speed;

	public SpeedExpression(Token firstToken, Expression array, Expression speed) {
		super(firstToken);
		this.array = array;
		this.speed = speed;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitSpeedExpression(this, argument);
	}

}
