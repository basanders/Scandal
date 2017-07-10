package language.tree;

import language.compiler.Token;

public class WriteStatement extends Statement {
	
	public final Expression name;
	public final Expression format;

	public WriteStatement(Token firstToken, Expression array, Expression name, Expression format) {
		super(firstToken, array);
		this.name = name;
		this.format = format;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitWriteStatement(this, arg);
	}

}
