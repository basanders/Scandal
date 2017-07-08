package language.tree;

import language.compiler.Token;

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
