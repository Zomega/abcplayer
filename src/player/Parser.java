package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import utilities.Fraction;

import lexer.*;

/**
 * Class to convert abc files into Piece data structures.
 * @author kimtoy
 * @version prealpha
 */
public class Parser {
    
    // Define TokenTypes
    public final static TokenType FIELD_NUM = new TokenType("FIELD_NUM",
            Pattern.compile("X:.*\\n"));
    public final static TokenType FIELD_TITLE = new TokenType("FIELD_TITLE",
            Pattern.compile("T:.*\\n"));
    public final static TokenType FIELD_COMP = new TokenType("FIELD_COMP",
            Pattern.compile("C:.*\\n"));
    public final static TokenType FIELD_DEFAULT_LEN = new TokenType("FIELD_DEFAULT_LEN",
            Pattern.compile("L:"));
    public final static TokenType FIELD_METER = new TokenType("FIELD_METER",
            Pattern.compile("M:"));
    public final static TokenType FIELD_TEMPO = new TokenType("FIELD_TEMPO",
            Pattern.compile("Q:"));
    public final static TokenType FIELD_VOICE = new TokenType("FIELD_VOICE",
            Pattern.compile("V:.*\\n"));
    public final static TokenType FIELD_KEY = new TokenType("FIELD_KEY",
            Pattern.compile("K:"));
    public final static TokenType BASENOTE = new TokenType("BASENOTE",
            Pattern.compile("[a-gA-G]{1}"));
    public final static TokenType KEY_ACCIDENTAL = new TokenType("KEY_ACCIDENTAL",
            Pattern.compile("[#b]"));
    public final static TokenType ACCIDENTAL = new TokenType("ACCIDENTAL",
            Pattern.compile("(\\^)|(\\^\\^)|(_)|(__)|(=)"));
    public final static TokenType MODE_MINOR = new TokenType("MODE_MINOR",
            Pattern.compile("m"));
    public final static TokenType METER = new TokenType("METER",
            Pattern.compile("(C)|(C\\|)"));
    public final static TokenType OCTAVE = new TokenType("OCTAVE",
            Pattern.compile("('+)|(,+)"));
    public final static TokenType DUPLET = new TokenType("DUPLET",
            Pattern.compile("\\(2"));
    public final static TokenType TUPLET = new TokenType("TUPLET",
            Pattern.compile("\\(3"));
    public final static TokenType QUADRUPLET = new TokenType("QUADRUPLET",
            Pattern.compile("\\(4"));
    public final static TokenType OPEN_REPEAT = new TokenType("OPEN_REPEAT",
            Pattern.compile("\\|:"));
    public final static TokenType CLOSE_REPEAT = new TokenType("CLOSE_REPEAT",
            Pattern.compile(":\\|"));
    public final static TokenType DOUBLE_BARLINE = new TokenType("DOUBLE_BARLINE",
            Pattern.compile("(\\|\\|)|(\\[\\|)|(\\|\\])"));
    public final static TokenType BARLINE = new TokenType("BARLINE",
            Pattern.compile("\\|"));
    public final static TokenType ONE_REPEAT = new TokenType("ONE_REPEAT",
            Pattern.compile("\\[1"));
    public final static TokenType TWO_REPEAT = new TokenType("TWO_REPEAT",
            Pattern.compile("\\[2"));
    public final static TokenType FRACTION = new TokenType("FRACTION",
            Pattern.compile("\\d+/\\d+"));
    public final static TokenType FRACTION_NOT_STRICT = new TokenType("FRACTION_NOT_STRICT",
            Pattern.compile("\\d*/\\d*"));
    public final static TokenType DIGITS = new TokenType("DIGITS",
            Pattern.compile("\\d+"));
    public final static TokenType REST = new TokenType("REST",
            Pattern.compile("z"));
    public final static TokenType OPEN_CHORD = new TokenType("OPEN_CHORD",
            Pattern.compile("\\["));
    public final static TokenType CLOSE_CHORD = new TokenType("CLOSE_CHORD",
            Pattern.compile("\\]"));
    public final static TokenType COMMENT = new TokenType("COMMENT",
            Pattern.compile("%.*\\n"));
    public final static TokenType NEWLINE = new TokenType("NEWLINE",
            Pattern.compile("\\n"));
    public final static TokenType SPACE = new TokenType("SPACE",
            Pattern.compile("[\\s]+"));
    
    //compile token types
    public static TokenType[] typeArray = {FIELD_NUM,FIELD_TITLE,FIELD_COMP,FIELD_DEFAULT_LEN,
    FIELD_METER,FIELD_TEMPO,FIELD_VOICE,FIELD_KEY,BASENOTE,KEY_ACCIDENTAL,ACCIDENTAL,MODE_MINOR,METER,OCTAVE,DUPLET,
    TUPLET,QUADRUPLET,OPEN_REPEAT,CLOSE_REPEAT,DOUBLE_BARLINE,BARLINE,ONE_REPEAT,TWO_REPEAT,FRACTION,FRACTION_NOT_STRICT,
    DIGITS,REST,OPEN_CHORD,CLOSE_CHORD,COMMENT,NEWLINE,SPACE};
    
    public static List<TokenType> types = new ArrayList<TokenType>(Arrays.asList(typeArray));
    
    /**
     * Parse the contents of an abc music file. 
     * @param abcContents
     * @return abc music file as represented by a Piece object
     */
	public static Piece parse( String abcContents ) { 
		
		Lexer l = new Lexer( types );
		List<Token> tokens = l.lex( abcContents );
		Iterator<Token> iter = tokens.iterator();
		Piece piece = new Piece();
		//Parse header
		Token next = parseHeaderInfo(piece, iter);
		//TODO: Iterate over tokens, adding Measures to the voices in piece.
		return piece;		
	}
	
	/**
	 * Parses the headers from the abc music file and returns the next non header token.
	 * If there are no more tokens and none are non-headers, returns null
	 * 
	 * @param piece
	 * @param iter
	 * @return
	 */
	public static Token parseHeaderInfo(Piece piece, Iterator<Token> iter){
	    //set defaults 
	    Fraction defaultLen = new Fraction(1,8);
        piece.setMeter(new Fraction(4,4));
        boolean setDefaultLenFlag = false;
        
        //Extract header information.
        while(iter.hasNext()){
            Token next = iter.next();
            if(next.type==FIELD_NUM){
                piece.setTrackNumber(Integer.parseInt(next.contents));
                //discard this information, the whole header line is one token
                System.out.println("Discard field num "+next.contents);
            }
            else if(next.type==FIELD_TITLE){
                piece.setTitle(next.contents);
                //discard this information, the whole header line is one token
                System.out.println("Discard field title "+next.contents);
            }
            else if(next.type==FIELD_COMP){
                piece.setComposer(next.contents);
                //discard this information, the whole header line is one token
                System.out.println("Discard field composer "+next.contents);
            }
            else if(next.type==FIELD_DEFAULT_LEN){
                next = eatSpaces(iter);
                if(next!=null&&next.type==FRACTION){
                    defaultLen=parseFraction(next.contents);
                    piece.setDefaultNoteLength(defaultLen);
                    setDefaultLenFlag = true;
                    if(!eatNewLine(iter))//next token should be end of line character to end the header field
                        throw new IllegalArgumentException("Field L: must be ended by an end of line character");
                    System.out.println("Set default length ");
                }
                else
                    throw new IllegalArgumentException("Field L: must be followed by a fraction note length "+next.type.name+ " "+next.contents);
            }
            else if(next.type==FIELD_METER){
                next = eatSpaces(iter);
                if(next!=null&&(next.type==METER||next.type==FRACTION)){
                    if(!next.contents.equals("C")&&!next.contents.equals("C|"))
                        piece.setMeter(parseFraction(next.contents));
                    if(!eatNewLine(iter))//next token should be end of line character to end the header field
                        throw new IllegalArgumentException("Field L: must be ended by an end of line character");
                    System.out.println("Set field meter");
                }
                else
                    throw new IllegalArgumentException("Field M: must be followed by a meter definition");
            }
            else if(next.type==FIELD_TEMPO){
                next = eatSpaces(iter);
                if(next!=null&&next.type==DIGITS){
                    piece.setTempo(Integer.parseInt(next.contents));
                    if(!eatNewLine(iter))//next token should be end of line character to end the header field
                        throw new IllegalArgumentException("Field L: must be ended by an end of line character");
                    System.out.println("Set field tempo");
                }
                else
                    throw new IllegalArgumentException("Field Q: must be followed by an integer tempo defintion");
            }
            else if(next.type==FIELD_VOICE){
                //TODO: ignoring voice header for now, set voices in piece later
                System.out.println("Saw field voice");
            }
            else if(next.type==FIELD_KEY){
                next = eatSpaces(iter);
                if(next.type!=null&&next.type==BASENOTE){
                    String key = next.contents;
                    key+=parseHeaderKey(iter);//find other key info and take out the end of line character
                    piece.setKey(key);
                    System.out.println("Set field key");
                    }
                else
                    throw new IllegalArgumentException("Field K: must be followed by a keynote");
                }
            else{
                if(setDefaultLenFlag==false)
                    piece.setDefaultNoteLength(defaultLen);
                System.out.println("Done with header");
                return next;
            }
        }
        return null;
	}
	
	/**
	 * Ignores whitespace tokens, excluding the newline character, that are spaces and returns the next non whitespace
	 * character.  Returns null if there none of the tokens are non whitespace and there are no tokens left to iterate over.
	 * @param iter
	 * @return
	 */
	public static Token eatSpaces(Iterator<Token> iter){
	    Token next;
	    while(iter.hasNext()){
	        next = iter.next();
	        if(next.type!=SPACE)
	            return next;
	    }
	    return null;
	}
	
	/**
	 * Should be called if next token is a newline, it will ignore the newline token and set the iterator on the next token. 
	 * Does nothing if there are no more tokens to iterate over.
	 * If the next token isn't a newline, it throws an exception.
	 * @param iter
	 */
	public static boolean eatNewLine(Iterator<Token> iter){
	    if(iter.hasNext()){
	        //if not a newline throw exception
	        if(iter.next().type!=NEWLINE)
	            return false;
	    }
	    return true;//if no more tokens or next token was a newline, do nothing.
	}
	
	/**
	 * Parses a fraction token and returns a Fraction represention
	 * @param f - must be a string of the regex form "\\d+/\\d+"
	 * @return
	 */
	public static Fraction parseFraction(String frac){
	    int slashPos = frac.indexOf('/');
    	int num = Integer.parseInt(frac.substring(0, slashPos));
    	int denom = Integer.parseInt(frac.substring(slashPos+1));
    	return new Fraction(num, denom);
	}
	
	/**
	 * Check and parse for other information in the key header, such as accidentals or minor mode.
	 * 
	 * @return parsed key accidental or minor info, else if there are no tokens left or no extra info, returns empty string
	 */
	public static String parseHeaderKey(Iterator<Token> iter){
	    Token next;
	    String key="";
	    boolean minor=false;//flag to indicate that mode minor token has been parsed
	    
	    while(iter.hasNext()){
	        next = iter.next();
	        if(next.type==KEY_ACCIDENTAL){
	            if(minor==false)
	                key+=next.contents;
	            else
	                throw new IllegalArgumentException("In field K: key accidental must be declared before minor mode");
	        }
	        else if(next.type==MODE_MINOR){
	            key+=next.contents;
	            minor = true;
	        }
	        else if(next.type==NEWLINE){
	            return key;
	        }
	        else
	            throw new IllegalArgumentException("Field K: must be ended by a newline character");
	    }
	    return key;
	}

}
