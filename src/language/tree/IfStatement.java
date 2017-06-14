package language.tree;

import language.compiler.Token;

public class IfStatement extends Statement {
	
	public final Block block;
	
	public IfStatement(Token firstToken, Expression expression, Block block) {
		super(firstToken, expression);
		this.block = block;
	}

}
