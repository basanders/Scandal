package language.tree;

import language.compiler.Token;

public class IntLitExpression extends Expression {

	public IntLitExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.INT;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitIntLitExpression(this, argument);
	}

}
