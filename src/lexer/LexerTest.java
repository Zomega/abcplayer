package lexer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * This JUnit Testing file is based off the contents of ps2.
 * 
 * @author woursler
 */
public class LexerTest {
	private TokenType INTEGER = new TokenType("INTEGER",
			Pattern.compile("\\d+(?!\\.)"));
	private TokenType FLOATING_POINT = new TokenType("FLOATING_POINT",
			Pattern.compile("(\\d*\\.\\d+(?!\\.))|(\\d+\\.\\d*(?!\\.))"));
	private TokenType VARIABLE = new TokenType("VARIABLE",
			Pattern.compile("[a-zA-Z]+"));
	private TokenType OPERATOR = new TokenType("OPERATOR",
			Pattern.compile("[*+]"));
	private TokenType OPEN_PAREN = new TokenType("OPEN_PAREN",
			Pattern.compile("\\("));
	private TokenType CLOSE_PAREN = new TokenType("CLOSE_PAREN",
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

	// TODO: Add a couple more tests...
	@Test
	public void test1() {
		Lexer l = new Lexer(types);
		List<Token> tokens = l.lex(" ((	4+3 )  *  (2.0 ))");
		//Check that all the types are equal (I'll assume the values...).
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
}
