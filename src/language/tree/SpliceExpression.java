package language.tree;

import java.util.ArrayList;

import language.compiler.Token;

public class SpliceExpression extends Expression {
	
	public final ArrayList<Expression> expressions;

	public SpliceExpression(Token firstToken, ArrayList<Expression> expressions) {
		super(firstToken);
		this.expressions = expressions;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitSpliceExpression(this, argument);
	}

}
