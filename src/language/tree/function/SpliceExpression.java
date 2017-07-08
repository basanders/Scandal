package language.tree.function;

import java.util.ArrayList;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;

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
