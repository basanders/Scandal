package language.tree.function;

import language.compiler.Token;
import language.tree.Expression;
import language.tree.NodeVisitor;

public class BiquadExpression extends Expression {
	
	public final Expression array;
	public final Expression cutoff;
	public final Expression resonance;
	public final Expression method;

	public BiquadExpression(Token firstToken, Expression array, Expression cutoff, Expression resonance, Expression method) {
		super(firstToken);
		this.array = array;
		this.cutoff = cutoff;
		this.resonance = resonance;
		this.method = method;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitBiquadExpression(this, arg);
	}

}
