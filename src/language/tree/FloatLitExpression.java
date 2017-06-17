package language.tree;

import language.compiler.Token;

public class FloatLitExpression extends Expression {
	
	public final float value;

	public FloatLitExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.FLOAT;
		value = firstToken.getFloatValue();
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitFloatLitExpression(this, argument);
	}

}
