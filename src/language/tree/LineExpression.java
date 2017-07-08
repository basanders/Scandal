package language.tree;

import language.compiler.Token;

public class LineExpression extends Expression {
	
	public final Expression size;
	public final Expression breakpoints;

	public LineExpression(Token firstToken, Expression size, Expression breakpoints) {
		super(firstToken);
		this.size = size;
		this.breakpoints = breakpoints;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitLineExpression(this, arg);
	}

}
