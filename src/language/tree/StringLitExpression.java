package language.tree;

import language.compiler.Token;

public class StringLitExpression extends Expression {

	public StringLitExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.STRING;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitStringLitExpression(this, argument);
	}

}
