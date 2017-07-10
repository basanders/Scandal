package language.tree;

import language.compiler.Token;

public class PanExpression extends Expression {
	
	public final Expression array;
	public final Expression position;

	public PanExpression(Token firstToken, Expression array, Expression position) {
		super(firstToken);
		this.array = array;
		this.position = position;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitPanExpression(this, arg);
	}

}
