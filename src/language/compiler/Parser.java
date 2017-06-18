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
		if (token.kind == KW_INT || token.kind == KW_FLOAT || token.kind == KW_BOOL) {			
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
		match(LPAREN);
		Expression expression = expression();
		match(RPAREN);
		Block block = block();
		return new IfStatement(firstToken, expression, block);
	}

	public WhileStatement whileStatement() throws Exception {
		Token firstToken = token;
		match(KW_WHILE);
		match(LPAREN);
		Expression expression = expression();
		match(RPAREN);
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
		switch (token.kind) {
		case IDENT: {
			expression = new IdentExpression(token);
			consume();
		} break;
		case INT_LIT: {
			expression = new IntLitExpression(token);
			consume();
		} break;
		case FLOAT_LIT: {
			expression = new FloatLitExpression(token);
			consume();
		} break;
		case KW_FALSE:
		case KW_TRUE: {
			expression = new BoolLitExpression(token);
			consume();
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
