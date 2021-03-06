package language.compiler;

import static language.compiler.Token.Kind.IDENT;
import static language.compiler.Token.Kind.KW_FLOAT;
import static language.compiler.Token.Kind.KW_IF;
import static language.compiler.Token.Kind.KW_INT;
import static language.compiler.Token.Kind.KW_WHILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import language.tree.AssignmentDeclaration;
import language.tree.AssignmentStatement;
import language.tree.BinaryExpression;
import language.tree.BoolLitExpression;
import language.tree.Declaration;
import language.tree.Expression;
import language.tree.FloatLitExpression;
import language.tree.IfStatement;
import language.tree.IntLitExpression;
import language.tree.Program;
import language.tree.ReadExpression;
import language.tree.WhileStatement;

public class ParserTests {
	
	@Test
	public void testDeclaration() throws Exception {
		String input = "int three";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		Declaration declaration = program.declarations.get(0);
		assertEquals(KW_INT, declaration.firstToken.kind);
		assertEquals(IDENT, declaration.identToken.kind);
	}
	
	@Test
	public void testAssignmentDeclaration() throws Exception {
		String input = "float pi = 3.14";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		AssignmentDeclaration declaration = (AssignmentDeclaration) program.declarations.get(0);
		assertEquals(KW_FLOAT, declaration.firstToken.kind);
		assertEquals(IDENT, declaration.identToken.kind);
		assertEquals(FloatLitExpression.class, declaration.expression.getClass());
	}
	
	@Test
	public void testAssignmentStatement() throws Exception {
		String input = "abc = 123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		AssignmentStatement statement = (AssignmentStatement) program.statements.get(0);
		assertEquals(IDENT, statement.firstToken.kind);
		assertEquals(IntLitExpression.class, statement.expression.getClass());
	}
	
	@Test
	public void testIfStatement() throws Exception {
		String input = "if (true) {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		IfStatement statement = (IfStatement) program.statements.get(0);
		assertEquals(KW_IF, statement.firstToken.kind);
		assertEquals(BoolLitExpression.class, statement.expression.getClass());
		assertNotNull(statement.block);
	}
	
	@Test
	public void testWhileStatement() throws Exception {
		String input = "while (2 + 2) {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		WhileStatement statement = (WhileStatement) program.statements.get(0);
		assertEquals(KW_WHILE, statement.firstToken.kind);
		assertEquals(BinaryExpression.class, statement.expression.getClass());
		assertNotNull(statement.block);
	}
	
	@Test
	public void testBinaryExpression() throws Exception {
		String input = "(1 * 2 / 3 % 4 & 5 + 6 - 7 | 8) < 9 <= 10 > 11.11 >= true == false != abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Expression expression = parser.expression();
		assertEquals(BinaryExpression.class, expression.getClass());
	}
	
	@Test
	public void testProgram() throws Exception {
		String input = "int three \n"
				+ "three = 3 \n"
				+ "float pi = 3.1415 \n"
				+ "if (three < pi) { three = pi - 0.1415 } \n";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		new Parser(scanner).parse();
	}

	@Test
	public void testWaveFileExpression() throws Exception {
		String input = " array sound = wave(\"fileName.wav\")";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Declaration declaration = new Parser(scanner).declaration();
		AssignmentDeclaration ad = (AssignmentDeclaration) declaration;
		Expression e = ad.expression;
		assertEquals(ReadExpression.class, e.getClass());
	}

}
