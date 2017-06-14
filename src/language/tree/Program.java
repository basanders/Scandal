package language.tree;

import java.util.ArrayList;

import language.compiler.Token;

public class Program extends Node {

	public final ArrayList<Declaration> declarations;
	public final ArrayList<Statement> statements;

	public Program(Token firstToken, ArrayList<Declaration> declarations, ArrayList<Statement> statements) {
		super(firstToken);
		this.declarations = declarations;
		this.statements = statements;
	}

}
