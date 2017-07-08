package language.tree;

import language.compiler.Token;

public class WaveformExpression extends Expression {
	
	public final int shape;

	public WaveformExpression(Token firstToken) {
		super(firstToken);
		this.shape = getShape(firstToken);
		this.type = Type.WAVEFORM;
	}
	
	private int getShape(Token token) {
		switch (token.kind) {
		case KW_COSINE: return 1;
		case KW_SAWTOOTH: return 2;
		case KW_SQUARE: return 3;
		case KW_TRIANGLE: return 4;
		case KW_NOISE: return 5;
		default: return 0;
		}
	}

	@Override
	public Object visit(NodeVisitor visitor, Object arg) throws Exception {
		return visitor.visitWaveformExpression(this, arg);
	}

}
