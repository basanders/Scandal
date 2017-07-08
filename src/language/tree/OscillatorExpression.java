package language.tree;

import language.compiler.Token;

public class OscillatorExpression extends Expression {
	
	public final Expression duration;
	public final Expression amplitude;
	public final Expression frequency;
	public final Expression shape;

	public OscillatorExpression(
			Token firstToken,
			Expression duration,
			Expression amplitude,
			Expression frequency,
			Expression shape) {
		super(firstToken);
		this.duration = duration;
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.shape = shape;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitOscillatorExpression(this, arg);
	}

}
