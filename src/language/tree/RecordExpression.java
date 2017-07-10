package language.tree;

import language.compiler.Token;

public class RecordExpression extends Expression {
	
	public final Expression duration;

	public RecordExpression(Token firstToken, Expression duration) {
		super(firstToken);
		this.duration = duration;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitRecordExpression(this, arg);
	}

}
