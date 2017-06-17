package language.tree;

import language.compiler.Token;

public class BoolLitExpression extends Expression {
	
	public final Boolean value;

	public BoolLitExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.BOOL;
		value = firstToken.text.equals("true");
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitBoolLitExpression(this, argument);
	}

}
