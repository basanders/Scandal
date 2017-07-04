package language.tree;

import language.compiler.Token;

public class LoopExpression extends Expression {
	
	public final Expression array;
	public final Expression start;
	public final Expression end;
	public final Expression count;

	public LoopExpression(Token firstToken, Expression array, Expression start, Expression end, Expression count) {
		super(firstToken);
		this.array = array;
		this.start = start;
		this.end = end;
		this.count = count;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitLoopExpression(this, argument);
	}

}
