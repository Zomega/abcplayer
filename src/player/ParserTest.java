package player;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import lexer.Token;

import org.junit.Test;

import sound.Pitch;
import utilities.Fraction;
import utilities.Pair;

/**
 * Parser test file
 * @author kimtoy
 * @category no_didit
 *
 */
public class ParserTest {
    
    @Test
    public void testParseHeader(){
        BufferedReader f = null;
        String line = "";
        try {
          f = new BufferedReader(new FileReader("sample_abc/fur_elise.abc"));
          String nextLine = f.readLine();
          while (nextLine != null) {
            line += nextLine + "\n";
            nextLine = f.readLine();
          }
          f.close();
        } catch (IOException e) {
          System.err.println("File couldn't be read");
        }
        System.out.println(line);
        
        Piece piece = Parser.parse(line);
        System.out.println("Default length "+piece.getDefaultNoteLength()+" meter "+piece.getMeter()+" tempo "+piece.getTempo()+" key "+piece.getKey());
    }
    
    /**
     * Test Parser.parseNoteElement on different note and modifier combinations
     * See that note parser catches the next token correctly
     */
    @Test
    public void testNoteParsing(){
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1,4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("D");
        
        List<Token> note1 = Parser.lex("G");
        assertEquals(new Pair<Token, Note>(null, new Note(new Fraction(1,4), new Pitch('G'))), Parser.parseNoteElement(piece, note1.iterator(), scale));
        
        List<Token> note2 = Parser.lex("G'");
        assertEquals(new Pair<Token, Note>(null, new Note(new Fraction(1,4), new Pitch('G').transpose(12))), Parser.parseNoteElement(piece, note2.iterator(), scale));
        
        List<Token> note3 = Parser.lex("C,1/8");
        assertEquals(new Pair<Token, Note>(null, new Note(new Fraction(1,8), new Pitch('C').transpose(-11))), Parser.parseNoteElement(piece, note3.iterator(), scale));
        
        List<Token> note4 = Parser.lex("=c''2");
        assertEquals(new Pair<Token, Note>(null, new Note(new Fraction(1,1), new Pitch('C').transpose(36))), Parser.parseNoteElement(piece, note4.iterator(), scale));
        
        List<Token> note5 = Parser.lex("_B,,/3");
        assertEquals(new Pair<Token, Note>(null, new Note(new Fraction(1,3), new Pitch('B').transpose(-25))), Parser.parseNoteElement(piece, note5.iterator(), scale));
        
        List<Token> note6 = Parser.lex("^^F/");
        assertEquals(new Pair<Token, Note>(null, new Note(new Fraction(1,2), new Pitch('F').transpose(2))), Parser.parseNoteElement(piece, note6.iterator(), scale));
        
        List<Token> note7 = Parser.lex("_b'4/4 ");
        assertEquals(new Pair<Token, Note>(new Token(" ",Parser.SPACE), new Note(new Fraction(1,1), new Pitch('B').transpose(23))), Parser.parseNoteElement(piece, note7.iterator(), scale));
    }
    
    /**
     * Illegal to mix octave modifers , and '
     */
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalOctaveModifiers(){
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1,4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("D");
        
        List<Token> note1 = Parser.lex("G,,'");
        Parser.parseNoteElement(piece, note1.iterator(), scale);
    }
    
    /**
     * Illegal to use multiple types of accidentals
     */
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalAccidentalStacking(){
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1,4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("D");
        
        List<Token> note1 = Parser.lex("^^_G");
        Parser.parseNoteElement(piece, note1.iterator(), scale);
    }
    
    /**
     * Test the parseFraction and parseFractionNotStrict methods
     * Parsing a non-strict fraction should return the appropriate values for note length values, i.e.
     * no denominator 
     */
    @Test
    public void testFractionParser(){
        assertEquals(new Fraction(12,20), Parser.parseFraction("12/20"));
        assertEquals(new Fraction(1,2), Parser.parseFractionNotStrict("/"));
        assertEquals(new Fraction(3,2), Parser.parseFractionNotStrict("3/"));
        assertEquals(new Fraction(1,11), Parser.parseFractionNotStrict("/11"));
    }
    
}
