package lexer;

import java.util.regex.Pattern;

/**
 * Defines a type of token. Note that TokenType is compared by reference, not
 * value. It is the responsibility of the user to ensure that there are no
 * duplicate TokenTypes.
 * 
 * @author woursler
 * 
 */
public class TokenType {
	public final String id;
	public final Pattern pattern;

	public TokenType(String id, Pattern pattern) {
		this.id = id;
		this.pattern = pattern;
	}
}
