package language.tree;

import java.util.ArrayList;

import language.compiler.Token;

public class Block extends Node {

	public final ArrayList<Declaration> declarations;
	public final ArrayList<Statement> statements;

	public Block(Token firstToken, ArrayList<Declaration> declarations, ArrayList<Statement> statements) {
		super(firstToken);
		this.declarations = declarations;
		this.statements = statements;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitBlock(this, argument);
	}

}
