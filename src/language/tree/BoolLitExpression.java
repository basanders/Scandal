package language.tree;

import language.compiler.Token;

public class BoolLitExpression extends Expression {

	public BoolLitExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.BOOL;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitBoolLitExpression(this, argument);
	}

}
