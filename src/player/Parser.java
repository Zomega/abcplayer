package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import sound.Pitch;
import utilities.Fraction;
import utilities.Pair;

import lexer.*;

/**
 * Class to convert abc files into Piece data structures.
 * @author kimtoy
 * @version prealpha
 */
public class Parser {
    
    // Define TokenTypes
    public final static TokenType FIELD_NUM = new TokenType("FIELD_NUM",
            Pattern.compile("X:\\s*\\d+\\n"));
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
            Pattern.compile("(\\^{1,2})|(_{1,2})|(=)"));
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
		if (tokens.size() < 2)
		{
		    throw new IllegalArgumentException("Field has invalid number of fields");
		}
		if (tokens.get(0).type != FIELD_NUM)
		{
		    throw new IllegalArgumentException("Header must start with Track Number");
		}
		if (tokens.get(1).type != FIELD_TITLE)
        {
            throw new IllegalArgumentException("2nd field in header must be Title");
        }
		
		Iterator<Token> iter = tokens.iterator();
		Piece piece = new Piece();
		//Parse header
		Token next = parseHeaderInfo(piece, iter);
		//Parse abc music lines, piece is expected to have one or more lines, so exception is thrown no more tokens
		if(next==null)
		    throw new IllegalArgumentException("File must contain at least one line of abc music.");
		
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
                int track = Integer.parseInt(next.contents.substring(2).trim());
                piece.setTrackNumber(track);
                System.out.println("Set track number to "+track);
            }
            else if(next.type==FIELD_TITLE){
                piece.setTitle(next.contents.substring(2).trim());
                System.out.println("Set field title to "+next.contents.substring(2).trim());
            }
            else if(next.type==FIELD_COMP){
                piece.setComposer(next.contents.substring(2).trim());
                System.out.println("Set field composer to "+next.contents.substring(2).trim());
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
                //These would be Voice Declarations
                //Add to our Piece's list of 'recognized' Voices
                piece.addVoice(new Voice(next.contents.substring(2).trim()));
                System.out.println("Made new Voice (no first measure) "+ next.contents.substring(2).trim());
            }
            else if(next.type==FIELD_KEY){
                next = eatSpaces(iter);
                if(next.type!=null&&next.type==BASENOTE){
                    String key = next.contents;
                    key+=parseHeaderKey(iter);//find other key info and take out the end of line character
                    piece.setKey(key);
                    System.out.println("Set field key");
                    //Key should be the final line in the header
                    //So from now on, we cannot have non-Notes/Voice Tokens
                    if(setDefaultLenFlag==false)
                    {
                        piece.setDefaultNoteLength(defaultLen);
                    }
                    System.out.println("Done with header");
                    return next;
                }
                else
                    throw new IllegalArgumentException("Field K: must be followed by a keynote");
                }
            else{
                //See the beginning of a musical line; requires a default voice too
                if(setDefaultLenFlag==false)
                    piece.setDefaultNoteLength(defaultLen);
                System.out.println("Done with header");
                return next;
            }
        }
        return null;
	}
	
	public static void parseABCLines(Piece piece, Iterator<Token> iter){
	    //TODO:Currently assuming single voice
	    //Voice and measure setup
	    HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature(piece.getKey());
	    //Measure mStart = new Measure(piece.getDefaultNoteLength());
	    //Voice vStart = new Voice("start", mStart);
	    
	    //Since this should be called right after parsing header, we can assume
	    //that the first line will be of musical notes or a voice.  
	    Voice currentVoice;
	    Measure currentMeasure = new Measure(piece.getDefaultNoteLength());
	    Measure lastOpen = currentMeasure;
	    Measure lastPreOne = currentMeasure;
	    //In order to know the Measure right before a first ending
	    
	    Fraction relTime = new Fraction(0);
	    //The relative position in the Measure we're in right now.  
	    
	    boolean startMusic = false;
	     
	    while (iter.hasNext())
	    {
	        Token next = iter.next();
	        if(next.type==FIELD_VOICE){
                boolean hasSeen = false;
	            for (int i = 0; i < piece.getVoices().size(); i++)
	            {
	                Voice v = piece.getVoices().get(i);
	                if (v.name == next.contents.substring(2).trim())
	                {
	                    currentVoice = v;
	                    hasSeen = true;
	                }
	            }
	            if (hasSeen == false)
	            {
	                piece.addVoice(new Voice(next.contents.substring(2).trim()));
	                System.out.println("Made new Voice (no first measure) "+ next.contents.substring(2).trim());
	                currentVoice = piece.getVoices().get(piece.getVoices().size() - 1);
	            }
	        }
	        else if (next.type == ACCIDENTAL || next.type == BASENOTE)
	        {
	            if (startMusic == false)
	            //the first time musical notes are read in
	            {
	                if (piece.getVoices().size() < 1)
	                {
	                    piece.addVoice(new Voice("Default", currentMeasure));
	                }
	                currentVoice = piece.getVoices().get(piece.getVoices().size() - 1);
	                startMusic = true;
	            }
	            //TODO: add the notes to the currentMeasure; change relTime as needed
	        }
	        else if (next.type == REST)
	        {
	            Fraction noteLength=null;
	            //TODO: Something like we do for Note; change relTime as needed
	        }
	        else if (next.type == BARLINE || next.type == DOUBLE_BARLINE)
	        {
	            lastPreOne = currentMeasure;
	            Measure newMeasure = new Measure(piece.getDefaultNoteLength());
	            currentMeasure.setNext(newMeasure);
	            
	            currentMeasure = newMeasure;
	            relTime = new Fraction(0);
                //reset the relative time to start of measure...
	        }
	        else if (next.type == CLOSE_REPEAT)
	        {
	            lastPreOne = currentMeasure;
	            //TODO: Implement this as a stack to deal with nesting.
	            
	            Measure newMeasure = new Measure(piece.getDefaultNoteLength());
	            currentMeasure.setNext(lastOpen);
	            //Has this measure first loop back to the last Open
	            currentMeasure.setAlternateNext(newMeasure);
	            //...before going to the next measure
	            currentMeasure = newMeasure;
	            //Now go onto the next (currently empty) measure...
	            lastOpen = newMeasure;
	            //Which will now be the open parenthesis for the next repeat
	            relTime = new Fraction(0);
	            //reset the relative time to start of measure...
	        }
	        else if (next.type == ONE_REPEAT)
	        {
	            //Do NOT set lastPreOne to this!!!  This pointer lets us
	            //link to the second ending.  
	            
	        }
	        else if (next.type == TWO_REPEAT)
            {
	            lastPreOne.setAlternateNext(currentMeasure);
	            //Tell the measure right before the first ending to go here next time
                lastPreOne = currentMeasure ;
                //Now this becomes the lastPreOne
            }
	        else
	        {
	            throw new IllegalArgumentException("Bad Tokens found in parsing music");
	        }
 
	        
	    }
	    //get the most 'recent' Voice as the starting currentVoice
	    
	    
	    //When reach Voice Token, see if it's already seen; if not, add to recog voices
	    //Then switch current Voice to that one.  
	    
	    //We assume music for each Voice is in a whole number of Measures
	}
	
	 /**
	  * Parses a note element, which includes a basenote, and may be preceded by an accidental or followed by an octave
	  * 
	  * @param measure - that the note element belongs to
	  * @param piece
	  * @param iter
	  */
	public static Pair<Token, Note> parseNoteElement(Piece piece, Iterator<Token> iter, HashMap<String, Pitch> scale){
	    Token next;
	    Pitch p = null;
	    Fraction noteLength=null;
	    
	    //pull first token, expected to be accidental or basenote
	    if(iter.hasNext()){
	        next = iter.next();
	        if(next.type==ACCIDENTAL)//if accidental, parse note pitch and correct accidental
	            p = parseAccidental(next, iter, scale);
	        else if(next.type==BASENOTE)//if only basenote, parse the note pitch with default accidental of 3
	            p = parseBasenote(next, 3, scale);
	        else//if basenote was not encountered, throw an exception
	            throw new IllegalArgumentException("Note element must contain a basenote");
	    }
	    //check for optional modifiers
	    if(iter.hasNext()){
	        next = iter.next();
	        if(next.type==OCTAVE){//if octave token, parse accordingly
	            p = parseOctave(next, p);
	            if(iter.hasNext()){//check if followed by note length token, else return next token
	                next = iter.next();
	                if(next.type==DIGITS||next.type==FRACTION||next.type==FRACTION_NOT_STRICT)
	                    noteLength = parseNoteLength(next);
	                else if(next.type==OCTAVE)
	                    throw new IllegalArgumentException("Note should not have mixed octave modifiers");
	                else
	                    return new Pair<Token, Note>(next, new Note(piece.getDefaultNoteLength(), p));
	            }
	        }
	        else if(next.type==DIGITS||next.type==FRACTION||next.type==FRACTION_NOT_STRICT)//is note length token
	            noteLength=parseNoteLength(next);
	        else
	            return new Pair<Token, Note>(next, new Note(piece.getDefaultNoteLength(), p));
	    }
	    //return next token and parsed Note
	    if(iter.hasNext()){
	        if(noteLength==null)//if note length was not set, set length to default value
	            return new Pair<Token, Note>(iter.next(), new Note(piece.getDefaultNoteLength(), p));
	        else
	            return new Pair<Token, Note>(iter.next(), new Note(noteLength, p));
	    }
	    else{
	        if(noteLength==null)
                return new Pair<Token, Note>(null, new Note(piece.getDefaultNoteLength(), p));
            else
                return new Pair<Token, Note>(null, new Note(noteLength, p));
	    }
	}
	
	/**
	 * Returns a pitch with correct accidental when passed a token that is an accidental
	 * @param next
	 * @param iter
	 * @param scale
	 * @return
	 */
	public static Pitch parseAccidental(Token next, Iterator<Token> iter, HashMap<String, Pitch> scale){
	    int accidental = 0;
	    if(next.contents.equals("^"))
            accidental = 1;
        else if(next.contents.equals("^^"))
            accidental = 2;
        else if(next.contents.equals("_"))
            accidental = -1;
        else if(next.contents.equals("__"))
            accidental = -2;
        else if(next.contents.equals("="))
            accidental = 0;
        else
            throw new IllegalArgumentException("Invalid type of accidental");
	    Token basenote;
	    if(iter.hasNext()){//parse basenote
	        basenote=iter.next();
	        if(basenote.type!=BASENOTE)
	            throw new IllegalArgumentException("Accidental must be followed by basenote");
	        return parseBasenote(basenote, accidental, scale);
	    }
	    else
	        throw new IllegalArgumentException("Accidental must be followed by basenote");
	}
	
	/**
	 * Returns a Pitch value correctly representing the Token next with the given accidental/key signature
	 * @param next
	 * @param accidental
	 * @param scale
	 * @return
	 */
	public static Pitch parseBasenote(Token next, int accidental, HashMap<String, Pitch> scale){
	    int octave = 0;
	    if(next.contents.equals(next.contents.toLowerCase())) //if lowercase (octave higher)
            octave = 12;//raise the pitch by 12 halfsteps for an octave
        if(accidental==3)//3 signifies default key value according to key signature, if there was no accidental
            return scale.get(next.contents.toUpperCase()).transpose(octave);
        else
            return new Pitch(next.contents.toUpperCase().toCharArray()[0]).transpose(accidental+octave);
	}
	
	/**
	 * Transposes pitch to correct octave given the pitch and an octave token
	 * Assumes that the octave token is valid (i.e. contains only commas or only apostrophes, not a mixture)
	 * @param next
	 * @param p
	 * @return
	 */
	public static Pitch parseOctave(Token next, Pitch p){
	    if(next.contents.contains(",")){
	        int octavesDown = next.contents.length();
	        return p.transpose(-octavesDown*12);
	    }
	    else{
	        int octavesUp = next.contents.length();
	        return p.transpose(octavesUp*12);
	    }
	}
	
	/**
	 * Returns a fraction representation of note length given either a DIGIT, FRACTION, or FRACTION_NOT_STRICT token
	 * @param next
	 * @return
	 */
	public static Fraction parseNoteLength(Token next){
	    if(next.type==DIGITS)
	        return new Fraction(Integer.parseInt(next.contents), 2);
	    else if(next.type==FRACTION)
	        return parseFraction(next.contents);
	    else if(next.type==FRACTION_NOT_STRICT)
	        return parseFractionNotStrict(next.contents);
	    else
	        throw new IllegalArgumentException("Token argument to parseNoteLength must be either digit or strict or non-strict fraction");
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
	 * Parses a FRACTION token and returns a Fraction represention
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
     * Parses a FRACTION_NOT_STRICT token and returns a Fraction representation
     * A non strict fraction could be 3/ or /3 or /
     * 
     * @param next
     * @return
     */
    public static Fraction parseFractionNotStrict(String frac){
        if(frac.equals("/"))
            return new Fraction(1,2);
        else if(frac.endsWith("/"))
            return new Fraction(Integer.parseInt(frac.substring(0, frac.length()-1)), 2);
        else //assume that token is of form /digits+
            return new Fraction(1, Integer.parseInt(frac.substring(1)));
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
	
	/**
	 * For testing purposes, lex a string into valid tokens
	 * @param string
	 * @return
	 */
	public static List<Token> lex(String string){
	    Lexer l = new Lexer( types );
	    return l.lex(string);
	}

}
