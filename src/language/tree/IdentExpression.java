package language.tree;

import language.compiler.Token;

public class IdentExpression extends Expression {

	public IdentExpression(Token firstToken) {
		super(firstToken);
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitIdentExpression(this, argument);
	}

}
