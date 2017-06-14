package language.compiler;

public class Token {

	public static enum Kind {
		EOF("eof"),
		IDENT(""),
		INT_LIT(""),
		FLOAT_LIT(""),
		// Single-character
		PLUS("+"),
		TIMES("*"),
		DIV("/"),
		MOD("%"),
		AND("&"),
		OR("|"),
		COMMA(","),
		DOT("."),
		LPAREN("("),
		RPAREN(")"),
		LBRACE("{"), 
		RBRACE("}"),
		// Single-character that precedes multiple-character
		ASSIGN("="),
		LT("<"),
		GT(">"),
		NOT("!"),
		MINUS("-"),
		// Multiple-character
		EQUAL("=="),
		LE("<="),
		GE(">="),
		NOTEQUAL("!="),
		ARROW("->"),
		// Keywords
		KW_INT("int"),
		KW_FLOAT("float"),
		KW_BOOL("bool"),
		KW_TRUE("true"),
		KW_FALSE("false"),
		KW_IF("if"),
		KW_WHILE("while");

		final String text;

		Kind(String text) {
			this.text = text;
		}
	}
	
	public Token(Kind kind, int position, int length, int lineNumber, int lineNumberPosition) {
		this.kind = kind;
		this.text = kind.text;
		this.position = position;
		this.length = length;
		this.lineNumber = lineNumber;
		this.lineNumberPosition = lineNumberPosition;
	}

	public Token(Kind kind, String text, int position, int length, int lineNumber, int lineNumberPosition) {
		this.kind = kind;
		this.text = text;
		this.position = position;
		this.length = length;
		this.lineNumber = lineNumber;
		this.lineNumberPosition = lineNumberPosition;
	}

	public final Kind kind;
	public final String text;
	public final int position; // position in input array
	public final int length;
	public final int lineNumber;
	public final int lineNumberPosition;

	public int getIntValue() throws NumberFormatException {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException exception) {
			throw new NumberFormatException("The token does not contain an integer.");
		}
	}

	public float getFloatValue() throws NumberFormatException {
		try {
			return Float.parseFloat(text);
		} catch (NumberFormatException exception) {
			throw new NumberFormatException("The token does not contain a float.");
		}
	}

}
