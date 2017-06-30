package language.tree;

import language.compiler.Token;

public class WaveFileExpression extends Expression {
	
	public final Expression expression;

	public WaveFileExpression(Token firstToken, Expression expression) {
		super(firstToken);
		this.expression = expression;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitWaveFileExpression(this, argument);
	}

}
