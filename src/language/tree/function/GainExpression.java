package language.tree.function;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;

public class GainExpression extends Expression {
	
	public final Expression array;
	public final Expression gain;

	public GainExpression(Token firstToken, Expression array, Expression gain) {
		super(firstToken);
		this.array = array;
		this.gain = gain;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitGainExpression(this, argument);
	}

}
