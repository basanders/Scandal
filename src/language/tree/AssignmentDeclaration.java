package language.tree;

import language.compiler.Token;

public class AssignmentDeclaration extends Declaration {
	
	public final Expression expression;

	public AssignmentDeclaration(Token firstToken, Token identToken, Expression expression) {
		super(firstToken, identToken);
		this.expression = expression;
	}	

}
