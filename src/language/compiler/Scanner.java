package language.compiler;

import java.util.ArrayList;

import language.compiler.Token.Kind;

public class Scanner {

	private final ArrayList<Token> tokens = new ArrayList<Token>();
	private final String chars;
	private int tokenNum;

	private static enum State { START, IDENT, DIGIT, FLOAT, STRING }

	public Scanner(String chars) {
		// Make sure there is always one character of look-ahead.
		this.chars = chars + ' ';
	}

	public Token nextToken() {
		if (tokenNum >= tokens.size()) return null;
		return tokens.get(tokenNum++);
	}

	private Token matchKeyword(String substring, int startPos, int endPos, int lineNum, int lineNumPos) {
		Token token;
		switch (substring) {
		case "int": {
			token = new Token(Kind.KW_INT, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "float": {
			token = new Token(Kind.KW_FLOAT, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "bool": {
			token = new Token(Kind.KW_BOOL, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "true": {
			token = new Token(Kind.KW_TRUE, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "false": {
			token = new Token(Kind.KW_FALSE, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "if": {
			token = new Token(Kind.KW_IF, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "while": {
			token = new Token(Kind.KW_WHILE, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "print": {
			token = new Token(Kind.KW_PRINT, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "info": {
			token = new Token(Kind.KW_INFO, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "string": {
			token = new Token(Kind.KW_STRING, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "array": {
			token = new Token(Kind.KW_ARRAY, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "read": {
			token = new Token(Kind.KW_READ, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "plot": {
			token = new Token(Kind.KW_PLOT, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "play": {
			token = new Token(Kind.KW_PLAY, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "mono": {
			token = new Token(Kind.KW_MONO, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "stereo": {
			token = new Token(Kind.KW_STEREO, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "format": {
			token = new Token(Kind.KW_FORMAT, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "reverse": {
			token = new Token(Kind.KW_REVERSE, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "speed": {
			token = new Token(Kind.KW_SPEED, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "loop": {
			token = new Token(Kind.KW_LOOP, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "splice": {
			token = new Token(Kind.KW_SPLICE, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "delay": {
			token = new Token(Kind.KW_DELAY, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "gain": {
			token = new Token(Kind.KW_GAIN, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "line": {
			token = new Token(Kind.KW_LINE, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "filter": {
			token = new Token(Kind.KW_FILTER, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "allpass": {
			token = new Token(Kind.KW_ALLPASS, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "bandpass": {
			token = new Token(Kind.KW_BANDPASS, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "bandstop": {
			token = new Token(Kind.KW_BANDSTOP, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "lowpass": {
			token = new Token(Kind.KW_LOWPASS, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "hipass": {
			token = new Token(Kind.KW_HIPASS, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "lowshelf": {
			token = new Token(Kind.KW_LOWSHELF, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "hishelf": {
			token = new Token(Kind.KW_HISHELF, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "peaking": {
			token = new Token(Kind.KW_PEAKING, startPos, endPos, lineNum, lineNumPos);
		} break;
		case "biquad": {
			token = new Token(Kind.KW_BIQUAD, startPos, endPos, lineNum, lineNumPos);
		} break;
		default: {
			token = new Token(Kind.IDENT, substring, startPos, endPos, lineNum, lineNumPos);
		} break;
		}
		return token;
	}

	public Scanner scan() throws Exception {
		State state = State.START;
		int pos = 0;
		int startPos = 0;
		int length = chars.length();
		int character;
		int lineNum = 0;
		int lineNumPos = 0;
		boolean skipCommentFlag = false;
		while (pos < length) {
			character = chars.charAt(pos);
			switch (state) {
			case START: {
				if (Character.isWhitespace(character)) {
					pos++;
					lineNumPos++;
					if (character == '\n') {
						lineNum++;
						lineNumPos = 0;
					}
					break;
				}
				if (skipCommentFlag && character == '*' && chars.charAt(pos + 1) == '/') {
					skipCommentFlag = false;
					pos += 2;
					lineNumPos += 2;
					break;
				}
				if (skipCommentFlag) {
					pos++;
					lineNumPos++;
					break;
				}
				if (character == '/' && chars.charAt(pos + 1) == '*') {
					skipCommentFlag = true;
					pos += 2;
					lineNumPos += 2;
					break;
				}
				startPos = pos;
				switch (character) {
				// Single-character
				case '+': {
					Token token = new Token(Kind.PLUS, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '*': {
					Token token = new Token(Kind.TIMES, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '/': {
					Token token = new Token(Kind.DIV, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '%': {
					Token token = new Token(Kind.MOD, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '&': {
					Token token = new Token(Kind.AND, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '|': {
					Token token = new Token(Kind.OR, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case ',': {
					Token token = new Token(Kind.COMMA, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '.': {
					Token token = new Token(Kind.DOT, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '(': {
					Token token = new Token(Kind.LPAREN, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case ')': {
					Token token = new Token(Kind.RPAREN, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '{': {
					Token token = new Token(Kind.LBRACE, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '}': {
					Token token = new Token(Kind.RBRACE, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case '[': {
					Token token = new Token(Kind.LBRACKET, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				case ']': {
					Token token = new Token(Kind.RBRACKET, startPos, 1, lineNum, lineNumPos);
					tokens.add(token);
					pos++;
					lineNumPos++;
				} break;
				// Single-character that precedes multiple-character
				case '=': {
					if (chars.charAt(pos + 1) == '=') {
						Token token = new Token(Kind.EQUAL, startPos, 2, lineNum, lineNumPos);
						tokens.add(token);
						pos += 2;
						lineNumPos += 2;
					} else {
						Token token = new Token(Kind.ASSIGN, startPos, 1, lineNum, lineNumPos);
						tokens.add(token);
						pos++;
						lineNumPos++;
					}
				} break;
				case '<': {
					if (chars.charAt(pos + 1) == '=') {
						Token token = new Token(Kind.LE, startPos, 2, lineNum, lineNumPos);
						tokens.add(token);
						pos += 2;
						lineNumPos += 2;
					} else {
						Token token = new Token(Kind.LT, startPos, 1, lineNum, lineNumPos);
						tokens.add(token);
						pos++;
						lineNumPos++;
					}
				} break;
				case '>': {
					if (chars.charAt(pos + 1) == '=') {
						Token token = new Token(Kind.GE, startPos, 2, lineNum, lineNumPos);
						tokens.add(token);
						pos += 2;
						lineNumPos += 2;
					} else {
						Token token = new Token(Kind.GT, startPos, 1, lineNum, lineNumPos);
						tokens.add(token);
						pos++;
						lineNumPos++;
					}
				} break;
				case '!': {
					if (chars.charAt(pos + 1) == '=') {
						Token token = new Token(Kind.NOTEQUAL, startPos, 2, lineNum, lineNumPos);
						tokens.add(token);
						pos += 2;
						lineNumPos += 2;
					} else {
						Token token = new Token(Kind.NOT, startPos, 1, lineNum, lineNumPos);
						tokens.add(token);
						pos++;
						lineNumPos++;
					}
				} break;
				case '-': {
					if (chars.charAt(pos + 1) == '>') {
						Token token = new Token(Kind.ARROW, startPos, 2, lineNum, lineNumPos);
						tokens.add(token);
						pos += 2;
						lineNumPos += 2;
					} else {
						Token token = new Token(Kind.MINUS, startPos, 1, lineNum, lineNumPos);
						tokens.add(token);
						pos++;
						lineNumPos++;
					}
				} break;
				case '0': {
					if (chars.charAt(pos + 1) == '.') {
						state = State.FLOAT;
						pos += 2;
						lineNumPos += 2;
					} else {
						Token token = new Token(Kind.INT_LIT, "0", startPos, 1, lineNum, lineNumPos);
						tokens.add(token);
						pos++;
						lineNumPos++;
					}
				} break;
				default: {
					if (Character.isDigit(character)) {
						state = State.DIGIT;
						pos++;
						lineNumPos++;
					} else if (Character.isJavaIdentifierStart(character)) {
						state = State.IDENT;
						pos++;
						lineNumPos++;
					} else if (character == '"') {
						state = State.STRING;
						pos++;
						lineNumPos++;
					} else {
						throw new Exception("Illegal character " + character + " at pos " + pos);
					}
				} break;
				} // switch (character)
			} break;
			case IDENT: {
				if (!Character.isJavaIdentifierPart(character)) {
					String substring = chars.substring(startPos, pos);
					Token token = matchKeyword(substring, startPos, pos - startPos,
							lineNum, lineNumPos - substring.length());
					tokens.add(token);
					state = State.START;
					break;
				}
				pos++;
				lineNumPos++;
			} break;
			case DIGIT: {
				if (!Character.isDigit(character) && character != '.') {
					String substring = chars.substring(startPos, pos);
					try {
						Integer.parseInt(substring);
					} catch (NumberFormatException exception) {
						throw new Exception("Illegal integer " + substring + " at pos " + pos);
					}
					Token token = new Token(Kind.INT_LIT, substring, startPos, pos - startPos,
							lineNum, lineNumPos - substring.length());
					tokens.add(token);
					state = State.START;
					break;
				}
				if (character == '.') state = State.FLOAT;
				pos++;
				lineNumPos++;
			} break;
			case FLOAT: {
				if (!Character.isDigit(character)) {
					String substring = chars.substring(startPos, pos);
					try {
						Float.parseFloat(substring);
					} catch (NumberFormatException exception) {
						throw new Exception("Illegal float " + substring + " at pos " + pos);
					}
					Token token = new Token(Kind.FLOAT_LIT, substring, startPos, pos - startPos,
							lineNum, lineNumPos - substring.length());
					tokens.add(token);
					state = State.START;
					break;
				}
				pos++;
				lineNumPos++;
			} break;
			case STRING: {
				if (character == '"') {
					String substring = String.valueOf(chars.substring(startPos + 1, pos));
					Token token = new Token(Kind.STRING_LIT, substring, startPos + 1, pos - startPos - 1,
							lineNum, lineNumPos - substring.length());
					tokens.add(token);
					state = State.START;
					pos++;
					lineNumPos++;
					break;
				}
				pos++;
				lineNumPos++;
			} break;
			default: assert false;
			} // switch (state)
		} // while (pos < length)
		tokens.add(new Token(Kind.EOF, pos - 1, 0, lineNum, lineNumPos - 1));
		return this;  
	}

}
