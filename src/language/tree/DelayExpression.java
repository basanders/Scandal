package language.tree;

import language.compiler.Token;

public class DelayExpression extends Expression {
	
	public final Expression time;
	public final Expression feedback;
	public final Expression mix;

	public DelayExpression(Token firstToken, Expression time, Expression feedback, Expression mix) {
		super(firstToken);
		this.time = time;
		this.feedback = feedback;
		this.mix = mix;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return null;
	}

}
