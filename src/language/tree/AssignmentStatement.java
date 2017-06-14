package language.tree;

import language.compiler.Token;

public class AssignmentStatement extends Statement {
	
	public AssignmentStatement(Token identToken, Expression expression) {
		super(identToken, expression);
	}

}
