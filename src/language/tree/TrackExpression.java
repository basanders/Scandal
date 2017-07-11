package language.tree;

import language.compiler.Token;

public class TrackExpression extends Expression {
	
	public final Expression array;
	public final Expression start;
	public final Expression gain;
	public final Expression pan;

	public TrackExpression(Token firstToken, Expression array, Expression start, Expression gain, Expression pan) {
		super(firstToken);
		this.array = array;
		this.start = start;
		this.gain = gain;
		this.pan = pan;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitTrackExpression(this, arg);
	}

}
