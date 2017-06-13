package language.scanner;

import java.util.ArrayList;

import language.scanner.Token.Kind;

public class Scanner {

	private final ArrayList<Token> tokens = new ArrayList<Token>();
	private final String chars;
	private int tokenNum;

	private static enum State {
		START,
		IDENT,
		DIGIT,
		AFTER_DOT,
		AFTER_EQUALS,
		AFTER_LT,
		AFTER_GT,
		AFTER_NOT,
		AFTER_MINUS
	}

	Scanner(String chars) {
		this.chars = chars + ' ';
	}

	public Token nextToken() {
		if (tokenNum >= tokens.size()) return null;
		return tokens.get(tokenNum++);
	}

	public Token peek(){
		if (tokenNum >= tokens.size()) return null;
		return tokens.get(tokenNum);
	}

	private Token matchKeyword(String substring, int startPosition, int endPosition, int lineNumber, int lineNumberPosition) {
		Token token;
		switch (substring) {
		case "int": {
			token = new Token(Kind.KW_INT, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		case "float": {
			token = new Token(Kind.KW_FLOAT, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		case "bool": {
			token = new Token(Kind.KW_BOOL, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		case "true": {
			token = new Token(Kind.KW_TRUE, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		case "false": {
			token = new Token(Kind.KW_FALSE, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		case "if": {
			token = new Token(Kind.KW_IF, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		case "while": {
			token = new Token(Kind.KW_WHILE, startPosition, endPosition, lineNumber, lineNumberPosition);
		} break;
		default: {
			token = new Token(Kind.IDENT, startPosition, endPosition, lineNumber, lineNumberPosition);
			token.substring = substring;
		} break;
		}
		return token;
	}

	public Scanner scan() throws Exception {
		State state = State.START;
		int position = 0;
		int startPosition = 0;
		int length = chars.length();
		int character;
		int lineNumber = 0;
		int lineNumberPosition = 0;
		boolean skipCommentFlag = false;
		while (position < length) {
			character = chars.charAt(position);
			switch (state) {
			case START: {
				if (Character.isWhitespace(character)) {
					position++;
					lineNumberPosition++;
					if (character == '\n') {
						lineNumber++;
						lineNumberPosition = 0;
					}
					break;
				}
				if (skipCommentFlag && character == '*' && position < length - 1 && chars.charAt(position + 1) == '/') {
					skipCommentFlag = false;
					position += 2;
					lineNumberPosition += 2;
					break;
				}
				if (skipCommentFlag) {
					position++;
					lineNumberPosition++;
					break;
				}
				if (character == '/' && position < length - 1 && chars.charAt(position + 1) == '*') {
					skipCommentFlag = true;
					position += 2;
					lineNumberPosition += 2;
					break;
				}
				startPosition = position;
				switch (character) {
				// Single-character
				case '+': {
					Token token = new Token(Kind.PLUS, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '*': {
					Token token = new Token(Kind.TIMES, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '/': {
					Token token = new Token(Kind.DIV, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '%': {
					Token token = new Token(Kind.MOD, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '&': {
					Token token = new Token(Kind.AND, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '|': {
					Token token = new Token(Kind.OR, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case ',': {
					Token token = new Token(Kind.COMMA, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '.': {
					Token token = new Token(Kind.DOT, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				case '(': {
					Token token = new Token(Kind.LPAREN, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;

				case ')': {
					Token token = new Token(Kind.RPAREN, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;

				case '{': {
					Token token = new Token(Kind.LBRACE, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;

				case '}': {
					Token token = new Token(Kind.RBRACE, startPosition, 1, lineNumber, lineNumberPosition);
					tokens.add(token);
					position++;
					lineNumberPosition++;
				} break;
				// Single-character that precedes multiple-character
				case '=': {
					state = State.AFTER_EQUALS;
					position++;
					lineNumberPosition++;
				} break;
				case '<': {
					state = State.AFTER_LT;
					position++;
					lineNumberPosition++;
				} break;
				case '>': {
					state = State.AFTER_GT;
					position++;
					lineNumberPosition++;
				} break;
				case '!': {
					state = State.AFTER_NOT;
					position++;
					lineNumberPosition++;
				} break;
				case '-': {
					state = State.AFTER_MINUS;
					position++;
					lineNumberPosition++;
				} break;
				case '0': {
					if (position < length - 1 && chars.charAt(position + 1) == '.') {
						state = State.AFTER_DOT;
						position += 2;
						lineNumberPosition += 2;
					} else {
						Token token = new Token(Kind.INT_LIT, startPosition, 1, lineNumber, lineNumberPosition);
						token.substring = "0";
						tokens.add(token);
						position++;
						lineNumberPosition++;
					}
				} break;
				default: {
					if (Character.isDigit(character)) {
						state = State.DIGIT;
						position++;
						lineNumberPosition++;
					} else if (Character.isJavaIdentifierStart(character)) {
						state = State.IDENT;
						position++;
						lineNumberPosition++;
					} else {
						throw new Exception("Illegal character " + character + " at position " + position);
					}

				}
				} // switch (character)
			} break; // case START
			case IDENT: {
				if (!Character.isJavaIdentifierPart(character) || position == length - 1) {
					String substring = chars.substring(startPosition, position);
					Token token = matchKeyword(substring, startPosition, position - startPosition,
							lineNumber, lineNumberPosition - substring.length());
					tokens.add(token);
					state = State.START;
				}
				position++;
				lineNumberPosition++;
			} break; // case IDENT
			case DIGIT: {
				if (!(Character.isDigit(character) || character == '.') || position == length - 1) {
					String substring = chars.substring(startPosition, position);
					try {
						Integer.parseInt(substring);
					} catch (NumberFormatException exception) {
						throw new Exception("Illegal integer " + substring + " at position " + position);
					}
					Token newToken = new Token(Kind.INT_LIT, startPosition, position - startPosition,
							lineNumber, lineNumberPosition - substring.length());
					newToken.substring = substring;
					tokens.add(newToken);
					state = State.START;
				}
				if (character == '.') state = State.AFTER_DOT;
				position++;
				lineNumberPosition++;
			} break; // case DIGIT
			case AFTER_DOT: {
				if (!Character.isDigit(character) || position == length - 1) {
					String substring = chars.substring(startPosition, position);
					try {
						Float.parseFloat(substring);
					} catch (NumberFormatException exception) {
						throw new Exception("Illegal float " + substring + " at position " + position);
					}
					Token newToken = new Token(Kind.FLOAT_LIT, startPosition, position - startPosition,
							lineNumber, lineNumberPosition - substring.length());
					newToken.substring = substring;
					tokens.add(newToken);
					state = State.START;
				}
				position++;
				lineNumberPosition++;
			} break; // case AFTER_DOT
			case AFTER_EQUALS: {
				if (character != '=' || position == length - 1) {
					Token token = new Token(Kind.ASSIGN, startPosition, 1, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				} else {
					Token token = new Token(Kind.EQUAL, startPosition, 2, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				}
				state = State.START;
				position++;
				lineNumberPosition++;
			} break; // case AFTER_EQUALS
			case AFTER_LT: {
				if (character != '=' || position == length - 1) {
					Token token = new Token(Kind.LT, startPosition, 1, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				} else {
					Token token = new Token(Kind.LE, startPosition, 2, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				}
				state = State.START;
				position++;
				lineNumberPosition++;
			} break; // case AFTER_LT
			case AFTER_GT: {
				if (character != '=' || position == length - 1) {
					Token token = new Token(Kind.GT, startPosition, 1, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				} else {
					Token token = new Token(Kind.GE, startPosition, 2, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				}
				state = State.START;
				position++;
				lineNumberPosition++;
			} break; // case AFTER_GT
			case AFTER_NOT: {
				if (character != '=' || position == length - 1) {
					Token token = new Token(Kind.NOT, startPosition, 1, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				} else {
					Token token = new Token(Kind.NOTEQUAL, startPosition, 2, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				}
				state = State.START;
				position++;
				lineNumberPosition++;
			} break; // case AFTER_NOT
			case AFTER_MINUS: {
				if (character != '>' || position == length - 1) {
					Token token = new Token(Kind.MINUS, startPosition, 1, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				} else {
					Token token = new Token(Kind.ARROW, startPosition, 2, lineNumber, lineNumberPosition - 1);
					tokens.add(token);
				}
				state = State.START;
				position++;
				lineNumberPosition++;
			} break; // case AFTER_MINUS
			default: assert false;
			} // switch (state)
		} // while (position < length)
		tokens.add(new Token(Kind.EOF, position - 1, 0, lineNumber, lineNumberPosition - 1));
		return this;  
	}

}
