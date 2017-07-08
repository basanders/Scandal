package language.tree.function;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;
import language.tree.Statement;

public class PlayStatement extends Statement {
	
	public final Expression format;

	public PlayStatement(Token firstToken, Expression expression, Expression format) {
		super(firstToken, expression);
		this.format = format;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitPlayStatement(this, argument);
	}

}
