package player;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import lexer.Token;

import org.junit.Test;

import sound.Pitch;
import utilities.Fraction;

/**
 * Parser test file
 * Testing strategy: 
 * Incrementally test different pieces of the parser from bottom up ensuring that helper functions are functional
 * before testing the larger functions.
 * Test that major parsing problems throw exceptions
 * 
 * Test suite includes the following (tests may be listed in an arbitrary order): 
 * -Fraction parsing tests
 * -Note parsing tests (accidental changes, octaves, duration)
 * -Note element parsing tests (chords, duplets, tuplets, quadruplets, rests)
 * -Measure parsing tests (length of measure, accidental changes)
 * -Header checks
 * 
 * @author kimtoy, czuo
 * @category no_didit
 * 
 */
public class ParserTest {

    @Test
    public void testParseHeader() throws NoteOutOfBoundsException {
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
        Piece piece = Parser.parse(line);
        assertEquals(1, piece.getTrackNumber());
        assertEquals("Piece No.1", piece.getTitle());
        assertEquals(new Fraction(4,4), piece.getMeter());
        assertEquals(new Fraction(1,4), piece.getDefaultNoteLength());
        assertEquals(140, piece.getTempo());
        assertEquals("C", piece.getKey());
    }

    @Test (expected=IllegalArgumentException.class)
    public void testHeadersRequired() throws NoteOutOfBoundsException{
        BufferedReader f = null;
        String line = "T: 0 \n 2 3 4 5|";
        Parser.parse(line);
    }
    
    public void testMeterField(){
        
    }
    
    public void testTitleComposerCanTakeAnyWhitespaceOrContentBeforeNewline(){}
    
    public void testHeaderStoresVoices(){}
    
    public void testABCFileMusicContainMusicalContent(){}
    
    public void testRepeatStructure() {
    }

    public void testMutlipleEndings() {
    }

    public void testPickup() {
    }

    public void testShorterEndingMeasure() {
    }

    public void testAccidentalKeyChangeWithinMeasureDoesntAffectOtherMeasures() {
    }
    
    /**
     * Test that having a rest element in a chord does not affect the other elements in the chord
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testRestsInChord() throws NoteOutOfBoundsException{
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("A [ABzD] C D|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) A, ( 1 / 4 ) >, < ( 1 / 4 ) B, ( 1 / 4 ) >, " +
        		"< ( 1 / 4 ) D, ( 1 / 4 ) >, < ( 1 / 4 ) C, ( 1 / 2 ) >, < ( 1 / 4 ) D, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }
    
    public void testRestsInTuplet(){}

    /**
     * Test Parser.parseNoteElement on different note and modifier combinations
     * See that note parser catches the next token correctly
     */
    @Test
    public void testNoteParsing() {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("D");
        Parser.accidentalChanges = new HashMap<Pitch, Pitch>();

        List<Token> note1 = Parser.lex("G");
        assertEquals(new Note(new Fraction(1, 4), new Pitch('G')),
                Parser.parseNoteElement(piece, note1.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note2 = Parser.lex("G'");
        assertEquals(
                new Note(new Fraction(1, 4), new Pitch('G').transpose(12)),
                Parser.parseNoteElement(piece, note2.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note3 = Parser.lex("C,1/8");
        assertEquals(
                new Note(new Fraction(1, 32), new Pitch('C').transpose(-11)),
                Parser.parseNoteElement(piece, note3.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note4 = Parser.lex("=c''2");
        assertEquals(
                new Note(new Fraction(1, 4), new Pitch('C').transpose(36)),
                Parser.parseNoteElement(piece, note4.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note5 = Parser.lex("_B,,/3");
        assertEquals(
                new Note(new Fraction(1, 12), new Pitch('B').transpose(-25)),
                Parser.parseNoteElement(piece, note5.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note6 = Parser.lex("^^F/");
        assertEquals(new Note(new Fraction(1, 8), new Pitch('F').transpose(2)),
                Parser.parseNoteElement(piece, note6.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note7 = Parser.lex("_b'4/4 ");
        assertEquals(
                new Note(new Fraction(1, 4), new Pitch('B').transpose(23)),
                Parser.parseNoteElement(piece, note7.listIterator(), scale,
                        new Fraction(1)));

        List<Token> note8 = Parser.lex("_b'4/4 ");
        assertEquals(
                new Note(new Fraction(1, 8), new Pitch('B').transpose(23)),
                Parser.parseNoteElement(piece, note8.listIterator(), scale,
                        new Fraction(1, 2)));
    }

    /**
     * Illegal to mix octave modifers , and '
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalOctaveModifiers() {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("D");
        Parser.accidentalChanges = new HashMap<Pitch, Pitch>();

        List<Token> note1 = Parser.lex("G,,'");
        Parser.parseNoteElement(piece, note1.listIterator(), scale,
                new Fraction(1));
    }

    /**
     * Illegal to use multiple types of accidentals
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalAccidentalStacking() {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("D");
        Parser.accidentalChanges = new HashMap<Pitch, Pitch>();

        List<Token> note1 = Parser.lex("^^_G");
        Parser.parseNoteElement(piece, note1.listIterator(), scale,
                new Fraction(1));
    }

    /**
     * Test the parseFraction and parseFractionNotStrict methods Parsing a
     * non-strict fraction should return the appropriate values for note length
     * values, i.e. no denominator
     */
    @Test
    public void testFractionParser() {
        assertEquals(new Fraction(12, 20), Parser.parseFraction("12/20"));
        assertEquals(new Fraction(1, 2), Parser.parseFractionNotStrict("/"));
        assertEquals(new Fraction(3, 2), Parser.parseFractionNotStrict("3/"));
        assertEquals(new Fraction(1, 11), Parser.parseFractionNotStrict("/11"));
    }

    /**
     * Test that parseMeasure can handle a basic measure of basenotes with no modifiers
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsing() throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("A B C D|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        String expected = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) B, ( 1 / 4 ) >, < ( 1 / 4 ) C, ( 1 / 2 ) >, "
                + "< ( 1 / 4 ) D, ( 3 / 4 ) >]";
        Parser.parseMeasureContents(piece, m, iter, scale);
        assertEquals(expected, m.toString());
    }

    /**
     * Test that a measure that is too long throws an exception
     * @throws NoteOutOfBoundsException
     */
    @Test(expected=NoteOutOfBoundsException.class)
    public void testMeasureOutOfBounds() throws NoteOutOfBoundsException{
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("A B C D E|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
    }
    
    /**
     * Test that parseMeasure can handle notes with accidental, octave, and duration modifiers
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingNotesWithModifiers()
            throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("^A, b1/8 f3/4 C1/8 _D F|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);

        String expected = "[< ( 1 / 4 ) ^A,, ( 0 / 1 ) >, < ( 1 / 32 ) b, ( 1 / 4 ) >, < ( 3 / 16 ) ^f, ( 9 / 32 ) >, "
                + "< ( 1 / 32 ) C, ( 15 / 32 ) >, < ( 1 / 4 ) ^C, ( 1 / 2 ) >, < ( 1 / 4 ) ^F, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * Test that adding rests to a measure results in a change in the start time of subsequent notes
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingRests() throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("z B C D|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 4 ) B, ( 1 / 4 ) >, < ( 1 / 4 ) C, ( 1 / 2 ) >, < ( 1 / 4 ) D, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * Test that notes inside a duplet are modified by 3/2 times their original length
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingDuplets() throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("A (2BD|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 3 / 8 ) B, ( 1 / 4 ) >, < ( 3 / 8 ) D, ( 5 / 8 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * Test that notes in a tuplet are made 2/3 of their original length
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingTuplets() throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("A1/3 (3^B1/4_D1/8^C1/8 f1/3|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 12 ) A, ( 0 / 1 ) >, < ( 1 / 24 ) c, ( 1 / 12 ) >, < ( 1 / 48 ) ^C, ( 1 / 8 ) >, " +
        		"< ( 1 / 48 ) ^C, ( 7 / 48 ) >, < ( 1 / 12 ) ^f, ( 1 / 6 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * Test that notes in a quadruplet are made 3/4 of their original length
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingQuadruplets() throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("z1/8 (4a''b''c''d'' z1/8|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 3 / 16 ) a'', ( 1 / 32 ) >, < ( 3 / 16 ) b'', ( 7 / 32 ) >, < ( 3 / 16 ) c'', " +
        		"( 13 / 32 ) >, < ( 3 / 16 ) d'', ( 19 / 32 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * Test that notes in a chord begin at the same starting time, and that subsequent notes begin
     * after the duration of the longest note in the chord
     * 
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingChords() throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("[CDE] F G A|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 4 ) C, ( 0 / 1 ) >, < ( 1 / 4 ) D, ( 0 / 1 ) >, < ( 1 / 4 ) E, ( 0 / 1 ) >, " +
        		"< ( 1 / 4 ) ^F, ( 1 / 4 ) >, < ( 1 / 4 ) G, ( 1 / 2 ) >, < ( 1 / 4 ) A, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * -Test that accidental changes within a measure affect other notes in the same octave
     * -Test that accidental changes do not affect notes with an accidental change itself, or notes in another octave
     * -Test that chords can handle notes with modifiers
     * -Test that subsequent notes begin after longest note in chord
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingChordsWithModifiersAndAccidentalChangesWithinMeasure()
            throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("[C1/8d'^^FF=Ff^E,1/8] F G A|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 32 ) C, ( 0 / 1 ) >, < ( 1 / 4 ) d', ( 0 / 1 ) >, < ( 1 / 4 ) G, ( 0 / 1 ) >, " +
        		"< ( 1 / 4 ) G, ( 0 / 1 ) >, < ( 1 / 4 ) F, ( 0 / 1 ) >, < ( 1 / 4 ) ^f, ( 0 / 1 ) >, < ( 1 / 32 ) F,, ( 0 / 1 ) >, " +
        		"< ( 1 / 4 ) F, ( 1 / 4 ) >, < ( 1 / 4 ) G, ( 1 / 2 ) >, < ( 1 / 4 ) A, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }

    /**
     * Test that a combination of different note elements is parsed correctly
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeasureParsingChordsTupletsRests()
            throws NoteOutOfBoundsException {
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("(3ABC [C1/8d'F1/3^E,1/8] z1/6|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure(piece.getMeter());
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 6 ) A, ( 0 / 1 ) >, < ( 1 / 6 ) B, ( 1 / 6 ) >, < ( 1 / 6 ) C, ( 1 / 3 ) >, " +
        		"< ( 1 / 32 ) C, ( 1 / 2 ) >, < ( 1 / 4 ) d', ( 1 / 2 ) >, < ( 1 / 12 ) ^F, ( 1 / 2 ) >, " +
        		"< ( 1 / 32 ) F,, ( 1 / 2 ) >]";
        assertEquals(expected, m.toString());
    }

}
