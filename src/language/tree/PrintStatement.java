package language.tree;

import language.compiler.Token;

public class PrintStatement extends Statement {

	public PrintStatement(Token firstToken, Expression expression) {
		super(firstToken, expression);
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitPrintStatement(this, argument);
	}

}
