package language.scanner;

import static language.scanner.Token.Kind.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScannerTests {
	
	@Test
	public void testEmptyString() throws Exception {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(EOF, token.kind);
		assertEquals(0, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(0, token.lineNumberPosition);
	}

	@Test(expected = Exception.class)
	public void testIllegalInput() throws Exception {
		String input = "^";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void testInteger() throws Exception {
		String input = " 0123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(1, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		assertEquals(0, token.getIntValue());
		token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(2, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(2, token.lineNumberPosition);
		assertEquals(123, token.getIntValue());
	}

	@Test(expected = Exception.class)
	public void testIntegerOverflow() throws Exception {
		String input = "9999999999";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void testFloat() throws Exception {
		String input = " 0555.123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(1, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		assertEquals(0, token.getIntValue());
		token = scanner.nextToken();
		assertEquals(FLOAT_LIT, token.kind);
		assertEquals(2, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(2, token.lineNumberPosition);
		assertEquals(555.123, token.getFloatValue(), 0.0001);
	}

	@Test
	public void testComment() throws Exception {
		String input = " 123 /* ^^^ */ 456";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(1, token.position);
		token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(15, token.position);
		token = scanner.nextToken();
		assertEquals(EOF, token.kind);
		assertEquals(18, token.position);
	}

	@Test
	public void testEndlessComment() throws Exception {
		String input = " 123 /* ^^^ 456";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(1, token.position);
		token = scanner.nextToken();
		assertEquals(EOF, token.kind);
		assertEquals(15, token.position);
	}

	@Test
	public void testIdentifier() throws Exception {
		String input = " abcd";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(IDENT, token.kind);
		assertEquals(1, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		assertEquals("abcd", token.getText());
		token = scanner.nextToken();
		assertEquals(EOF, token.kind);
		assertEquals(5, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(5, token.lineNumberPosition);
	}

	@Test
	public void testSingleCharacter() throws Exception {
		String input = " + * / % & | \n , . ( ) { }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(PLUS, token.kind);
		assertEquals(1, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(TIMES, token.kind);
		assertEquals(3, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(3, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(DIV, token.kind);
		assertEquals(5, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(5, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(MOD, token.kind);
		assertEquals(7, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(7, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(AND, token.kind);
		assertEquals(9, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(9, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(OR, token.kind);
		assertEquals(11, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(11, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(COMMA, token.kind);
		assertEquals(15, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(DOT, token.kind);
		assertEquals(17, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(3, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(LPAREN, token.kind);
		assertEquals(19, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(5, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(RPAREN, token.kind);
		assertEquals(21, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(7, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(LBRACE, token.kind);
		assertEquals(23, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(9, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(RBRACE, token.kind);
		assertEquals(25, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(11, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(EOF, token.kind);
		assertEquals(26, token.position);
		assertEquals(1, token.lineNumber);
		assertEquals(12, token.lineNumberPosition);
	}

	@Test
	public void testMultipleCharacter() throws Exception {
		String input = " = < > ! - == <= >= != ->";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(ASSIGN, token.kind);
		assertEquals(1, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(LT, token.kind);
		assertEquals(3, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(3, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(GT, token.kind);
		assertEquals(5, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(5, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(NOT, token.kind);
		assertEquals(7, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(7, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(MINUS, token.kind);
		assertEquals(9, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(9, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(EQUAL, token.kind);
		assertEquals(11, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(11, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(LE, token.kind);
		assertEquals(14, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(14, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(GE, token.kind);
		assertEquals(17, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(17, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(NOTEQUAL, token.kind);
		assertEquals(20, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(20, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(ARROW, token.kind);
		assertEquals(23, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(23, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(EOF, token.kind);
		assertEquals(25, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(25, token.lineNumberPosition);
	}
	
	@Test
	public void testKeyword() throws Exception {
		String input = " int float bool true false if while";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token token = scanner.nextToken();
		assertEquals(KW_INT, token.kind);
		assertEquals(1, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(1, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(KW_FLOAT, token.kind);
		assertEquals(5, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(5, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(KW_BOOL, token.kind);
		assertEquals(11, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(11, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(KW_TRUE, token.kind);
		assertEquals(16, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(16, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(KW_FALSE, token.kind);
		assertEquals(21, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(21, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(KW_IF, token.kind);
		assertEquals(27, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(27, token.lineNumberPosition);
		token = scanner.nextToken();
		assertEquals(KW_WHILE, token.kind);
		assertEquals(30, token.position);
		assertEquals(0, token.lineNumber);
		assertEquals(30, token.lineNumberPosition);
	}

}
