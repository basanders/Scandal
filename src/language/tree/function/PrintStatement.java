package language.tree.function;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;
import language.tree.Statement;

public class PrintStatement extends Statement {

	public PrintStatement(Token firstToken, Expression expression) {
		super(firstToken, expression);
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitPrintStatement(this, argument);
	}

}
