package language.compiler;

import static language.tree.Node.Type.INT;
import static language.tree.Node.Type.FLOAT;
import static language.tree.Node.Type.BOOL;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import language.tree.Expression;

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

}
