package language.tree;

import language.compiler.Token;

public class TremoloExpression extends Expression {
	
	public final Expression array;
	public final Expression depth;
	public final Expression speed;
	public final Expression shape;

	public TremoloExpression(Token firstToken, Expression array, Expression depth, Expression speed, Expression shape) {
		super(firstToken);
		this.array = array;
		this.depth = depth;
		this.speed = speed;
		this.shape = shape;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitTremoloExpression(this, arg);
	}

}
