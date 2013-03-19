package player;

import java.util.ArrayList;
import java.util.List;

import lexer.*;

/**
 * Class to convert abc files into Piece data structures.
 * @version prealpha
 */
public class Parser {
	public static Piece parse( String abcContents ) {
		
		// TODO: Define TokenTypes
		// Define the valid types of abc tokens.
		List<TokenType> types = new ArrayList<TokenType>();
		
		Lexer l = new Lexer( types );
		
		List<Token> tokens = l.lex( abcContents );
		//TODO: Extract header information.
		Piece piece = new Piece();
		//TODO: Iterate over tokens, adding Measures to the voices in piece.
		return piece;		
	}

}
