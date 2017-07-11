package language.tree;

import java.util.ArrayList;

import language.compiler.Token;

public class MixExpression extends Expression {
	
	public final ArrayList<Expression> tracks;

	public MixExpression(Token firstToken, ArrayList<Expression> tracks) {
		super(firstToken);
		this.tracks = tracks;
		this.type = Type.ARRAY;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitMixExpression(this, arg);
	}

}
