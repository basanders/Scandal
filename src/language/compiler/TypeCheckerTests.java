package language.compiler;

import static language.tree.Node.Type.INT;
import static language.tree.Node.Type.FLOAT;
import static language.tree.Node.Type.BOOL;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import language.tree.Expression;
import language.tree.Program;

public class TypeCheckerTests {
	
	@Test
	public void testBinaryExpression1() throws Exception {
		String input = "1 % 2 + 3 - 4 * 5 / 6";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Expression expression = parser.expression();
		TypeChecker checker = new TypeChecker();
		expression.visit(checker, null);
		assertEquals(INT, expression.type);
	}
	
	@Test
	public void testBinaryExpression2() throws Exception {
		String input = "1.1 + 2.2 - 3.3 * 4.4 / 5.5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Expression expression = parser.expression();
		TypeChecker checker = new TypeChecker();
		expression.visit(checker, null);
		assertEquals(FLOAT, expression.type);
	}
	
	@Test
	public void testBinaryExpression3() throws Exception {
		String input = "1.1 + 2 + 3.3 - 4 - 5.5 * 6 * 7.7 / 8 / 9.9";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Expression expression = parser.expression();
		TypeChecker checker = new TypeChecker();
		expression.visit(checker, null);
		assertEquals(FLOAT, expression.type);
	}
	
	@Test
	public void testBinaryExpression4() throws Exception {
		String input = "1 + 2.2 + 3 - 4.4 - 5 * 6.6 * 7 / 8.8 / 9";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Expression expression = parser.expression();
		TypeChecker checker = new TypeChecker();
		expression.visit(checker, null);
		assertEquals(FLOAT, expression.type);
	}
	
	@Test
	public void testBinaryExpression5() throws Exception {
		String input = "(1 < 2.2) <= (3.3 > 4) >= (5 == 6) != (7.7 == 8.8) >= true > false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Expression expression = parser.expression();
		TypeChecker checker = new TypeChecker();
		expression.visit(checker, null);
		assertEquals(BOOL, expression.type);
	}
	
	@Test
	public void testAssignmentDeclaration() throws Exception {
		String input = "int three = 3 float pi pi = 3.14 float sum sum = three + 3.14";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		TypeChecker checker = new TypeChecker();
		program.visit(checker, null);
	}
	
	@Test
	public void testIfStatement() throws Exception {
		String input = "int three = 3 float pi = 3.14 float sum if (three < pi) { sum = three + 3.14 }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		TypeChecker checker = new TypeChecker();
		program.visit(checker, null);
	}
	
	@Test
	public void testProgram() throws Exception {
		String input = "int p = 15 int q = 2 bool test = true bool result "
				+ "while (q < p) { if (p % q == 0) { test = false q = p - 1 } q = q + 1 } result = test";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Program program = parser.parse();
		TypeChecker checker = new TypeChecker();
		program.visit(checker, null);
	}

}
