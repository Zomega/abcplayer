package player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.Scanner;

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
	public static List<Token> lex(String input) throws RuntimeException {
		List<Token> tokens = new ArrayList<Token>();
		Scanner scanner = new Scanner(input);
		scanner.useDelimiter("\\p{javaWhitespace}+");

		// TODO: Remove Scanner ( which has some lexing functions, even if I
		// don't use them ) and recognize and strip whitespace automagically?

		while (scanner.hasNext()) {
			String current = scanner.next();

			while (current.length() > 0) {

				boolean foundValidToken = false;

				for (Token.Type type : Token.Type.values()) {
					Matcher matcher = Token.getPattern(type).matcher(current);
					if (matcher.lookingAt()) {
						tokens.add(new Token(current.substring(matcher.start(),
								matcher.end()), type));
						current = current.substring(matcher.end());
						foundValidToken = true;
						break;
					}
				}

				if (!foundValidToken) {
					throw new RuntimeException("Invalid token in \"" + current
							+ "\".");
				}
			}
		}
		return tokens;
	}
}
