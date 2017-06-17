package language.tree;

import language.compiler.Token;

public class UnassignedDeclaration extends Declaration {

	public UnassignedDeclaration(Token firstToken, Token identToken) {
		super(firstToken, identToken);
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitUnassignedDeclaration(this, argument);
	}

}
