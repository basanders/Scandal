package language.compiler;

import static language.compiler.Token.Kind.*;

import java.util.ArrayList;

import language.compiler.Token.Kind;
import language.tree.*;

public class Parser {

	private final Scanner scanner;
	private Token token;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		token = scanner.nextToken();
	}

	private Token consume() throws Exception {
		Token temp = token;
		token = scanner.nextToken();
		return temp;
	}

	private Token match(Kind kind) throws Exception {
		if (token.kind == kind) return consume();
		throw new Exception("Saw " + token.kind + " expected " + kind + " at " + token.lineNumberPosition);
	}

	private Token matchEOF() throws Exception {
		if (token.kind == EOF) return token;
		throw new Exception("Expected EOF");
	}

	public Program parse() throws Exception {
		Token firstToken = token;
		ArrayList<Declaration> declarations = new ArrayList<>();
		ArrayList<Statement> statements = new ArrayList<>();
		while (token.kind != EOF) {
			try {
				Declaration declaration = declaration();
				declarations.add(declaration);
			} catch (Exception declarationException) {
				try {
					Statement statement = statement();
					statements.add(statement);
				} catch (Exception statementException) {
					throw new Exception("Illegal program: " + token.lineNumberPosition);
				}
			}
		}
		matchEOF();
		return new Program(firstToken, declarations, statements);
	}

	public Block block() throws Exception {
		Token firstToken = token;
		ArrayList<Declaration> declarations = new ArrayList<>();
		ArrayList<Statement> statements = new ArrayList<>();
		match(LBRACE);
		while (token.kind != RBRACE) {
			try {
				Declaration declaration = declaration();
				declarations.add(declaration);
			} catch (Exception declarationException) {
				try {
					Statement statement = statement();
					statements.add(statement);
				} catch (Exception statementException) {
					throw new Exception("Illegal block: " + token.lineNumberPosition);
				}
			}
		}
		match(RBRACE);
		return new Block(firstToken, declarations, statements);
	}

	public Declaration declaration() throws Exception {
		Token firstToken = token;
		if (
				token.kind == KW_INT |
				token.kind == KW_FLOAT |
				token.kind == KW_BOOL |
				token.kind == KW_STRING |
				token.kind == KW_FORMAT |
				token.kind == KW_FILTER |
				token.kind == KW_WAVEFORM |
				token.kind == KW_ARRAY) {			
			consume();
			Token identToken = token;
			match(IDENT);
			if (token.kind == ASSIGN) {
				match(ASSIGN);
				Expression expression = expression();
				return new AssignmentDeclaration(firstToken, identToken, expression);
			} else {
				return new UnassignedDeclaration(firstToken, identToken);
			}
		} else {
			throw new Exception("Illegal declaration: " + token.lineNumberPosition);
		}
	}

	public Statement statement() throws Exception {
		Statement statement;
		switch (token.kind) {
		case IDENT: {
			statement = assignmentStatement();
		} break;
		case KW_IF: {
			statement = ifStatement();
		} break;
		case KW_WHILE: {
			statement = whileStatement();
		} break;
		case KW_PRINT: {
			statement = printStatement();
		} break;
		case KW_PLOT: {
			statement = plotStatement();
		} break;
		case KW_PLAY: {
			statement = playStatement();
		} break;
		default: throw new Exception("Illegal statement: " + token.lineNumberPosition);
		}
		return statement;
	}

	public AssignmentStatement assignmentStatement() throws Exception {
		Token firstToken = token;
		match(IDENT);
		match(ASSIGN);
		Expression expression = expression();
		return new AssignmentStatement(firstToken, expression);
	}

	public IfStatement ifStatement() throws Exception {
		Token firstToken = token;
		match(KW_IF);
		Expression expression = expression();
		Block block = block();
		return new IfStatement(firstToken, expression, block);
	}

	public WhileStatement whileStatement() throws Exception {
		Token firstToken = token;
		match(KW_WHILE);
		Expression expression = expression();
		Block block = block();
		return new WhileStatement(firstToken, expression, block);
	}
	
	public PrintStatement printStatement() throws Exception {
		Token firstToken = token;
		match(KW_PRINT);
		match(LPAREN);
		Expression expression = expression();
		match(RPAREN);
		return new PrintStatement(firstToken, expression);
	}
	
	public PlotStatement plotStatement() throws Exception {
		Token firstToken = token;
		match(KW_PLOT);
		match(LPAREN);
		Expression title = expression();
		match(COMMA);
		Expression array = expression();
		match(COMMA);
		Expression points = expression();
		match(RPAREN);
		return new PlotStatement(firstToken, title, array, points);
	}
	
	public PlayStatement playStatement() throws Exception {
		Token firstToken = token;
		match(KW_PLAY);
		match(LPAREN);
		Expression array = expression();
		match(COMMA);
		Expression format = expression();
		match(RPAREN);
		return new PlayStatement(firstToken, array, format);
	}

	public Expression expression() throws Exception {
		Token firstToken = token;
		Expression e0;
		Token operator;
		Expression e1;
		e0 = term();
		while (
				token.kind == LT |
				token.kind == LE |
				token.kind == GT |
				token.kind == GE |
				token.kind == EQUAL |
				token.kind == NOTEQUAL) {
			operator = token;
			consume();
			e1 = term();
			e0 = new BinaryExpression(firstToken, e0, operator, e1);
		}
		return e0;
	}

	private Expression term() throws Exception {
		Token firstToken = token;
		Expression e0;
		Token operator;
		Expression e1;
		e0 = summand();
		while (token.kind == PLUS | token.kind == MINUS | token.kind == OR) {
			operator = token;
			consume();
			e1 = summand();
			e0 = new BinaryExpression(firstToken, e0, operator, e1);
		}
		return e0;
	}

	private Expression summand() throws Exception {
		Token firstToken = token;
		Expression e0;
		Token operator;
		Expression e1;
		e0 = factor();
		while (token.kind == TIMES | token.kind == DIV | token.kind == MOD | token.kind == AND) {
			operator = token;
			consume();
			e1 = factor();
			e0 = new BinaryExpression(firstToken, e0, operator, e1);
		}
		return e0;
	}

	private Expression factor() throws Exception {
		Expression expression;
		Token firstToken = token;
		switch (token.kind) {
		case IDENT: {
			consume();
			expression = new IdentExpression(firstToken);
		} break;
		case KW_INFO: {
			consume();
			expression = new InfoExpression(firstToken);
		} break;
		case INT_LIT: {
			consume();
			expression = new IntLitExpression(firstToken);
		} break;
		case FLOAT_LIT: {
			consume();
			expression = new FloatLitExpression(firstToken);
		} break;
		case STRING_LIT: {
			consume();
			expression = new StringLitExpression(firstToken);
		} break;
		case LBRACKET: {
			consume();
			ArrayList<Float> floats = new ArrayList<>();
			while (token.kind != RBRACKET) {
				if (token.kind == INT_LIT || token.kind == FLOAT_LIT) {
					if (token.kind == INT_LIT) floats.add((float) token.getIntValue());
					else if (token.kind == FLOAT_LIT) floats.add(token.getFloatValue());
					consume();
					try {
						match(COMMA);
					} catch (Exception e) {
						break;
					}
				} else {
					throw new Exception("Illegal factor: " + token.lineNumberPosition);
				}
			}
			expression = new ArrayLitExpression(firstToken, floats);
			match(RBRACKET);
		} break;
		case KW_READ: {
			consume();
			match(LPAREN);
			Expression fileName = expression();
			match(COMMA);
			Expression format = expression();
			match(RPAREN);
			expression = new ReadExpression(firstToken, fileName, format);
		} break;
		case KW_REVERSE: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(RPAREN);
			expression = new ReverseExpression(firstToken, array);
		} break;
		case KW_SPEED: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(COMMA);
			Expression speed = expression();
			match(RPAREN);
			expression = new SpeedExpression(firstToken, array, speed);
		} break;
		case KW_LOOP: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(COMMA);
			Expression start = expression();
			match(COMMA);
			Expression end = expression();
			match(COMMA);
			Expression count = expression();
			match(RPAREN);
			expression = new LoopExpression(firstToken, array, start, end, count);
		} break;
		case KW_DELAY: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(COMMA);
			Expression time = expression();
			match(COMMA);
			Expression feedback = expression();
			match(COMMA);
			Expression mix = expression();
			match(RPAREN);
			expression = new DelayExpression(firstToken, array, time, feedback, mix);
		} break;
		case KW_OSCILLATOR: {
			consume();
			match(LPAREN);
			Expression duration = expression();
			match(COMMA);
			Expression amplitude = expression();
			match(COMMA);
			Expression frequency = expression();
			match(COMMA);
			Expression shape = expression();
			match(RPAREN);
			expression = new OscillatorExpression(firstToken, duration, amplitude, frequency, shape);
		} break;
		case KW_BIQUAD: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(COMMA);
			Expression cutoff = expression();
			match(COMMA);
			Expression resonance = expression();
			match(COMMA);
			Expression method = expression();
			match(RPAREN);
			expression = new BiquadExpression(firstToken, array, cutoff, resonance, method);
		} break;
		case KW_GAIN: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(COMMA);
			Expression gain = expression();
			match(RPAREN);
			expression = new GainExpression(firstToken, array, gain);
		} break;
		case KW_LINE: {
			consume();
			match(LPAREN);
			Expression size = expression();
			match(COMMA);
			Expression breakpoints = expression();
			match(RPAREN);
			expression = new LineExpression(firstToken, size, breakpoints);
		} break;
		case KW_TREMOLO: {
			consume();
			match(LPAREN);
			Expression array = expression();
			match(COMMA);
			Expression depth = expression();
			match(COMMA);
			Expression speed = expression();
			match(COMMA);
			Expression shape = expression();
			match(RPAREN);
			expression = new TremoloExpression(firstToken, array, depth, speed, shape);
		} break;
		case KW_SPLICE: {
			consume();
			match(LPAREN);
			ArrayList<Expression> expressions = new ArrayList<>();
			while (token.kind != RPAREN) {
				Expression expr = expression();
				expressions.add(expr);
				try {
					match(COMMA);
				} catch (Exception e) {
					break;
				}
			}
			match(RPAREN);
			expression = new SpliceExpression(firstToken, expressions);
		} break;
		case KW_MONO:
		case KW_STEREO: {
			consume();
			expression = new FormatExpression(firstToken);
		} break;
		case KW_ALLPASS:
		case KW_BANDPASS:
		case KW_BANDSTOP:
		case KW_LOWPASS:
		case KW_HIPASS:
		case KW_LOWSHELF:
		case KW_HISHELF:
		case KW_PEAKING: {
			consume();
			expression = new FilterExpression(firstToken);
		} break;
		case KW_COSINE:
		case KW_SAWTOOTH:
		case KW_SQUARE:
		case KW_TRIANGLE:
		case KW_NOISE: {
			consume();
			expression = new WaveformExpression(firstToken);
		} break;
		case KW_FALSE:
		case KW_TRUE: {
			consume();
			expression = new BoolLitExpression(firstToken);
		} break;
		case LPAREN: {
			consume();
			expression = expression();
			match(RPAREN);
		} break;
		default:
			throw new Exception("Illegal factor: " + token.lineNumberPosition);
		}
		return expression;
	}

}
