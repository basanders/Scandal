package language.tree;

import language.compiler.Token;

public class AssignmentStatement extends Statement {

	public AssignmentStatement(Token identToken, Expression expression) {
		super(identToken, expression);
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitAssignmentStatement(this, argument);
	}

}
