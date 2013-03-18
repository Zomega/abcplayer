package player;

import java.util.regex.Pattern;

/**
 * A token is a lexical item that the parser uses.
 * 
 * At the time of writing, this is woursler's PS 2 token class, unchanged...
 * 
 * @author woursler
 * 
 */
public class Token {
	/**
	 * All the types of valid tokens.
	 */
	public static enum Type {
		VARIABLE, INTEGER, FLOATING_POINT, OPERATOR, OPEN_PAREN, CLOSE_PAREN
	}

	/**
	 * Method to access a definition for each possible token type.
	 * 
	 * @param type
	 *            a Token.Type to look up.
	 * @return a Pattern containing a regex that will match any valid token of
	 *         this type. Will return null if an invalid argument is given.
	 */
	public static Pattern getPattern(Token.Type type) {
		switch (type) {
		case INTEGER:
			return Pattern.compile("\\d+(?!\\.)");
		case FLOATING_POINT:
			return Pattern.compile("(\\d*\\.\\d+(?!\\.))|(\\d+\\.\\d*(?!\\.))");
		case VARIABLE:
			return Pattern.compile("[a-zA-Z]+");
		case OPERATOR:
			return Pattern.compile("[*+]");
		case OPEN_PAREN:
			return Pattern.compile("\\(");
		case CLOSE_PAREN:
			return Pattern.compile("\\)");
		default:
			return null;
		}
	}

	private final Type type;
	private final String contents;

	/**
	 * Basic constructor for a Token instance.
	 * 
	 * @param contents
	 *            the String that makes up the token.
	 * @param type
	 *            the Token.Type that this token is.
	 */
	public Token(String contents, Token.Type type) {
		this.type = type;
		this.contents = contents;
	}

	/**
	 * @return the Token.Type of this token.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the contents of this token.
	 */
	public String getContents() {
		return contents;
	}

	public String toString() {
		return contents;
	}
}
