package language.tree;

import java.util.ArrayList;

import language.compiler.Token;

public class ArrayLitExpression extends Expression {
	
	public final ArrayList<Float> floats;

	public ArrayLitExpression(Token firstToken, ArrayList<Float> floats) {
		super(firstToken);
		this.floats = floats;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitArrayLitExpression(this, arg);
	}

}
