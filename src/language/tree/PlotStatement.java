package language.tree;

import language.compiler.Token;

public class PlotStatement extends Statement {
	
	public final Expression array;
	public final Expression points;

	public PlotStatement(Token firstToken, Expression title, Expression array, Expression points) {
		super(firstToken, title);
		this.array = array;
		this.points = points;
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitPlotStatement(this, argument);
	}

}
