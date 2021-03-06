package language.tree;

import framework.utilities.Settings;
import language.compiler.Token;

public class InfoExpression extends Expression {
	
	public final String value;

	public InfoExpression(Token firstToken) {
		super(firstToken);
		this.type = Type.STRING;
		this.value = Settings.getInfo();
	}

	@Override
	public Object visit(NodeVisitor visitor, Object argument) throws Exception {
		return visitor.visitInfoExpression(this, argument);
	}

}
