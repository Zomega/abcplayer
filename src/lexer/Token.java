package lexer;

/**
 * A token is a lexical item that the parser uses.
 * 
 * At the time of writing, this is modified version of woursler's PS 2 token class, unchanged...
 * 
 * @author woursler
 * 
 */
public class Token {
	public final TokenType type;
	public final String contents;

	/**
	 * Basic constructor for a Token instance.
	 * 
	 * @param contents
	 *            the String that makes up the token.
	 * @param type
	 *            the Token.Type that this token is.
	 */
	public Token(String contents, TokenType type) {
		this.type = type;
		this.contents = contents;
	}

	public String toString() {
		return contents;
	}
}
