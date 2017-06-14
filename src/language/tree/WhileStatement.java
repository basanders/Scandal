package language.tree;

import language.compiler.Token;

public class WhileStatement extends Statement {
	
	public final Block block;
	
	public WhileStatement(Token firstToken, Expression expression, Block block) {
		super(firstToken, expression);
		this.block = block;
	}

}
