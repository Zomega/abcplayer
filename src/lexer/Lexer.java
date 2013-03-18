package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * A lexer takes a string and splits it into Tokens that are meaningful to a
 * parser.
 * 
 * Valid tokens are defined by the Token Class.
 * 
 * At the time of writing, this is woursler's PS 2 token class, unchanged...
 * 
 * @author woursler
 * 
 */
public class Lexer {

	private final List<TokenType> types;

	public Lexer(List<TokenType> types) {
		this.types = types;
	}

	/**
	 * Lexes the passed string into Tokens.
	 * 
	 * @param input
	 *            a string to be split into Tokens.
	 * @return a List of Tokens.
	 * @throws RuntimeException
	 *             if something that does appear to be a valid token is
	 *             encountered.
	 */
	public List<Token> lex(String input) throws RuntimeException {
		List<Token> tokens = new ArrayList<Token>();

		// Ensure we have a non-whitespace character right off the bat.
		String current = input.trim();

		while (current.length() > 0) {

			boolean foundValidToken = false;

			for (TokenType type : this.types) {
				Matcher matcher = type.pattern.matcher(current);
				if (matcher.lookingAt()) {
					tokens.add(new Token(current.substring(matcher.start(),
							matcher.end()), type));
					// Remove the token (and any whitespace trailing it) from
					// the string...
					current = current.substring(matcher.end()).trim();
					foundValidToken = true;
					break;
				}
			}

			if (!foundValidToken) {
				throw new RuntimeException("Invalid token in \"" + current
						+ "\".");
			}
		}
		return tokens;
	}
}
