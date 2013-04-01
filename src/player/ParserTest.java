package player;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import lexer.Token;

import org.junit.Test;

import sound.Pitch;
import utilities.Fraction;
import utilities.Pair;

/**
 * Parser test file
 * @author kimtoy, czuo
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
    
    @Test
    public void testBasic1() {
        //("A B C D|")
        List<String> expected = new ArrayList<String>();
        expected.add("< ( 1 / 4 ) A, ( 0 / 1 ) >");
        expected.add("< ( 1 / 4 ) B, ( 1 / 4 ) >");
        expected.add("< ( 1 / 4 ) C, ( 1 / 2 ) >");
        expected.add("< ( 1 / 4 ) D, ( 3 / 4 ) >");
    }
    
    @Test
    public void testRest1() {
        //"z B C D|"
        List<String> expected = new ArrayList<String>();
        expected.add("< ( 1 / 4 ) B, ( 1 / 4 ) >");
        expected.add("< ( 1 / 4 ) C, ( 1 / 2 ) >");
        expected.add("< ( 1 / 4 ) D, ( 3 / 4 ) >");
    }
    
    @Test
    public void testAccidentals1() {
        //("^A, b1/8 C1/8 _D F|");
        List<String> expected = new ArrayList<String>();
        expected.add("< ( 1 / 4 ) ^A,, ( 0 / 1 ) >");
        expected.add("< ( 1 / 8 ) b, ( 1 / 4 ) >");
        expected.add("< ( 1 / 8 ) C, ( 3 / 8 ) >");
        expected.add("< ( 1 / 4 ) ^C, ( 1 / 2 ) >");
        expected.add("< ( 1 / 4 ) ^F, ( 3 / 4 ) >");
    }
    
    @Test
    public void testDuplet1() {
        //"A (2BD|"
        List<String> expected = new ArrayList<String>();
        expected.add("< ( 1 / 4 ) A, ( 0 / 1 ) >");
        expected.add("< ( 3 / 8 ) B, ( 1 / 4 ) >");
        expected.add("< ( 3 / 8 ) D, ( 5 / 8 ) >");
    }
    
    @Test
    public void testMixed1() {
        //"A1/3 (3^B1/4_D1/8^C1/8 f1/3|"
        List<String> expected = new ArrayList<String>();
        expected.add("< ( 1 / 3 ) A, ( 0 / 1 ) >");
        expected.add("< ( 1 / 6 ) c, ( 1 / 3 ) >");
        expected.add("< ( 1 / 12 ) ^C, ( 1 / 2 ) >");
        expected.add("< ( 1 / 12 ) ^C, ( 7 / 12 ) >");
        expected.add("< ( 1 / 3 ) ^f, ( 2 / 3 ) >");
    }
    
    @Test
    public void testMixed2() {
        //"z1/8 (4a''b''c''d'' z1/8|"
        List<String> expected = new ArrayList<String>();
        expected.add("< ( 3 / 16 ) a'', ( 1 / 8 ) >");
        expected.add("< ( 3 / 16 ) b'', ( 5 / 16 ) >");
        expected.add("< ( 3 / 16 ) c'', ( 1 / 2 ) >");
        expected.add("< ( 3 / 16 ) d'', ( 11 / 16 ) >");
    }
    
}
