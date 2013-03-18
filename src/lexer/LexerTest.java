package lexer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * This JUnit Testing file is based off the contents of ps2.
 * 
 * @author woursler
 * @version beta
 */
public class LexerTest {
	public final TokenType INTEGER = new TokenType("INTEGER",
			Pattern.compile("\\d+(?!\\.)"));
	public final TokenType FLOATING_POINT = new TokenType("FLOATING_POINT",
			Pattern.compile("(\\d*\\.\\d+(?!\\.))|(\\d+\\.\\d*(?!\\.))"));
	public final TokenType VARIABLE = new TokenType("VARIABLE",
			Pattern.compile("[a-zA-Z]+"));
	public final TokenType OPERATOR = new TokenType("OPERATOR",
			Pattern.compile("[*+]"));
	public final TokenType OPEN_PAREN = new TokenType("OPEN_PAREN",
			Pattern.compile("\\("));
	public final TokenType CLOSE_PAREN = new TokenType("CLOSE_PAREN",
			Pattern.compile("\\)"));

	List<TokenType> types = new ArrayList<TokenType>();

	@Before
	public void setUp() {
		types.add(INTEGER);
		types.add(FLOATING_POINT);
		types.add(VARIABLE);
		types.add(OPERATOR);
		types.add(OPEN_PAREN);
		types.add(CLOSE_PAREN);
	}

	@Test
	public void test1() {
		Lexer l = new Lexer(types);
		List<Token> tokens = l.lex(" ((	4+3 )  *  (2.0 ))");
		// Check that all the types are equal (I'll assume the values...).
		assertEquals(tokens.get(0).type, OPEN_PAREN);
		assertEquals(tokens.get(1).type, OPEN_PAREN);
		assertEquals(tokens.get(2).type, INTEGER);
		assertEquals(tokens.get(3).type, OPERATOR);
		assertEquals(tokens.get(4).type, INTEGER);
		assertEquals(tokens.get(5).type, CLOSE_PAREN);
		assertEquals(tokens.get(6).type, OPERATOR);
		assertEquals(tokens.get(7).type, OPEN_PAREN);
		assertEquals(tokens.get(8).type, FLOATING_POINT);
		assertEquals(tokens.get(9).type, CLOSE_PAREN);
		assertEquals(tokens.get(10).type, CLOSE_PAREN);
	}

	@Test
	public void test2() {
		Lexer l = new Lexer(types);
		List<Token> tokens = l.lex("(((x * x) * x) + x)");
		// Check that all the types are equal.
		assertEquals(tokens.get(0).type, OPEN_PAREN);
		assertEquals(tokens.get(1).type, OPEN_PAREN);
		assertEquals(tokens.get(2).type, OPEN_PAREN);
		assertEquals(tokens.get(3).type, VARIABLE);
		assertEquals(tokens.get(4).type, OPERATOR);
		assertEquals(tokens.get(5).type, VARIABLE);
		assertEquals(tokens.get(6).type, CLOSE_PAREN);
		assertEquals(tokens.get(7).type, OPERATOR);
		assertEquals(tokens.get(8).type, VARIABLE);
		assertEquals(tokens.get(9).type, CLOSE_PAREN);
		assertEquals(tokens.get(10).type, OPERATOR);
		assertEquals(tokens.get(11).type, VARIABLE);
		assertEquals(tokens.get(12).type, CLOSE_PAREN);

		// Check a few values...
		assertEquals(tokens.get(5).contents, "x");
		assertEquals(tokens.get(4).contents, "*");
		assertEquals(tokens.get(10).contents, "+");
		assertEquals(tokens.get(12).contents, ")");

	}
}
