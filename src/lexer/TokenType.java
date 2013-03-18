package lexer;

import java.util.regex.Pattern;

/**
 * Defines a type of token. Note that TokenType is compared by reference, not
 * value. It is the responsibility of the user to ensure that there are no
 * duplicate TokenTypes.
 * 
 * @author woursler
 * @version RC1
 */
public class TokenType {
	public final String name;
	public final Pattern pattern;

	public TokenType(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}
}
