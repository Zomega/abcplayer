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
 * -Repeat checks
 * 
 * @author kimtoy, czuo
 * 
 */
public class ParserTest {

    /**
     * Test that the header information is correctly set from the parsed piece data
     * @throws NoteOutOfBoundsException
     */
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
        assertEquals("Bagatelle No.25 in A, WoO.59", piece.getTitle());
        assertEquals(new Fraction(3,8), piece.getMeter());
        assertEquals(new Fraction(1,16), piece.getDefaultNoteLength());
        assertEquals(240, piece.getTempo());
        assertEquals("Am", piece.getKey());
    }

    /**
     * Tests that exception is thrown if the required headers aren't set 
     * @throws NoteOutOfBoundsException
     */
    @Test (expected=IllegalArgumentException.class)
    public void testHeadersRequired() throws NoteOutOfBoundsException{
        String line = "T: 0 \n 2 3 4 5|";
        Parser.parse(line);
    }
    
    /**
     * Test that an exception is thrown when the piece has no track number
     * A runtime exception should be thrown for having an invalid token.
     * @throws NoteOutOfBoundsException
     */
    @Test (expected=RuntimeException.class)
    public void testNoTrackNumber() throws NoteOutOfBoundsException{
        String line = "X: \nT: title\n M:C\nK:C\n|A B C D|";
        Parser.parse(line);
    }
    
    /**
     * Test that Meter recognizes "C" as a meter
     * @throws NoteOutOfBoundsException
     */
    @Test
    public void testMeterFieldRecognizesCommonTimeSymbol() throws NoteOutOfBoundsException{
        Piece piece = Parser.parse("X: 2\nT: title\n M:C\nK:C\n|A B C D|");
        assertEquals(new Fraction(4,4), piece.getMeter());
    }
    
    /**
     * Test that the default note length is 1/8
     */
    @Test 
    public void testDefaultNoteLength(){
        Piece piece = Parser.parse("X: 2\nT: title\n M:C\nK:C\n|A B C D|");
        assertEquals(new Fraction(1,8), piece.getDefaultNoteLength());
    }
    
    /**
     * Test if the Title field trims whitespace before storing the piece's title
     * 
     * Ensure that a default voice was initialized, and musical content
     * was added to the piece
     */
    @Test
    public void testTitleCanTakeAnyWhitespaceOrContentBeforeNewlineAndDefaultVoiceCreated(){
        Piece piece = Parser.parse("X: 1\nT: My TITLE is L:ongCat It is long.   \n L:1/4\nM:C\nK:C\n|A B C D|");
        String expected = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) B, ( 1 / 4 ) >, < ( 1 / 4 ) C, ( 1 / 2 ) >, "
                + "< ( 1 / 4 ) D, ( 3 / 4 ) >]";
        assertEquals("My TITLE is L:ongCat It is long.", piece.getTitle());
        assertEquals(1, piece.getVoices().size());
        assertEquals(expected, piece.getVoices().get(0).getStart().getNext().toString());
    }
    
    /**
     * If voices declared, test that exception is thrown for unlabed voice
     */
    @Test(expected=RuntimeException.class)
    public void testUnlabedVoicesThrowsException(){
        Parser.parse("X: 2\nT: title\n V:1\nM:C\nK:C\n|A B C D|");
    }
    
    /**
     * Tests if voices defined in header were parsed and stored
     */
    @Test
    public void testHeaderStoresVoices(){
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n|A B C D|");
        assertEquals(3, piece.getVoices().size());
    }
    
    /**
     * Tests that exception is thrown when abc music file doesn't have musical content
     */
    @Test(expected=IllegalArgumentException.class)
    public void testABCFileMusicContainMusicalContent(){
        Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n");
    }
    
    /**
     * Test that no exceptions are thrown if there are extra newline characters in the header or
     * between the header and the body
     */
    @Test
    public void testCanParseWithExtraSpacesAndNewlinesInHeader(){
        Parser.parse("X: 1\nT: \n V:1 \n V:2 \n \n\nV:3 \n M:C  \n L:1/4 \nK:C \nV:1\n\n|A B C D|");
    }
    
    /**
     * Test that no exceptions are thrown if there are extra newline characters in the header
     */
    @Test
    public void testCanParseWithExtraNewlinesInBody(){
        Parser.parse("X: 1\nT: \n V:1 \n V:2\nV:3\n M:C\nK:C\nV:1\n|A B C D|\n|A B C D|");
    }
    
    /**
     * Test simple repeat structure |: stuff :|
     */
    @Test
    public void testRepeatStructure() {
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n |:A B C D|B C D E:|");
        Voice defaultVoice = piece.getVoices().get(0);
        Measure measure1 = defaultVoice.getStart().getNext();
        Measure measure2 = measure1.getNext();
        assertEquals(measure1, measure2.getNext());
    }
    
    /**
     * Test that a repeat with no opening brace starts from the start of the piece
     */
    @Test
    public void testRepeatWithNoOpeningBraceStartsFromStartOfPiece(){
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n A B C D|B C D E:|");
        Voice defaultVoice = piece.getVoices().get(0);
        Measure measure1 = defaultVoice.getStart();
        Measure measure2 = measure1.getNext();
        assertEquals(measure1, measure2.getNext());
    }
    
    /**
     * Test that repeats without an opening repeat bar will start from the last double bar line
     */
    @Test
    public void testRepeatWithNoOpeningBraceStartsFromDoubleBar(){
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n A B C D||B C D E:|");
        Voice defaultVoice = piece.getVoices().get(0);
        Measure measure1 = defaultVoice.getStart();
        Measure measure2 = measure1.getNext();
        assertEquals(measure2, measure2.getNext());
    }

    /**
     * Test that the multiple ending structure works
     */
    @Test
    public void testMutlipleEndings() {
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n L:1/4\nM:C\nK:C\nV:1\n D E F G|[1B C D E:|[2A B C D|");
        Voice defaultVoice = piece.getVoices().get(0);
        Measure measure1 = defaultVoice.getStart().getNext();
        Measure measure2 = measure1.getNext();
        String measure3 = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) B, ( 1 / 4 ) >, < ( 1 / 4 ) C, ( 1 / 2 ) >, "
                + "< ( 1 / 4 ) D, ( 3 / 4 ) >]";
        assertEquals(measure1, measure2.getNext());
        assertEquals(measure3, measure1.getAlternateNext().toString());
    }

    /**
     * Test that the pickup measure if indeed shorter than the default length
     * and doesn't throw errors
     */
    @Test
    public void testPickupMeasureHasShorterDuration() {
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n A B|");
        assertEquals(new Fraction(1, 4), piece.getVoices().get(0).getStart().getDuration());
    }
    
    /**
     * Test that accidental key changes within a measure do not affect other measures
     * It is tested in other cases whether accidentals propagate within a measure
     */
    @Test
    public void testAccidentalKeyChangeWithinMeasureDoesntAffectOtherMeasures() {
        System.out.println(Parser.lex("X: 1\nT: \n V:1 \n V:2\n V:3\n L:1/4 \nM:C\nK:C\nV:1\n A ^B|A B"));
        Piece piece = Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n L:1/4 \nM:C\nK:C\nV:1\n A ^B|A B");

        String measure1toString = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) c, ( 1 / 4 ) >]";
        String measure2toString = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) B, ( 1 / 4 ) >]";
        Voice defaultVoice = piece.getVoices().get(0);
        Measure measure1 = defaultVoice.getStart();
        Measure measure2 = measure1.getNext();
        assertEquals(measure1toString, measure1.toString());
        assertEquals(measure2toString, measure2.toString());
    }
    
    /**
     * Test that no error is thrown for measure that starts with a barline
     */
    @Test
    public void testCanParseMeasureThatStartsWithBarline(){
        Parser.parse("X: 1\nT: \n V:1 \n V:2\n V:3\n M:C\nK:C\nV:1\n||A B C D|");
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
        Measure m = new Measure();
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 4 ) A, ( 1 / 4 ) >, < ( 1 / 4 ) B, ( 1 / 4 ) >, " +
        		"< ( 1 / 4 ) D, ( 1 / 4 ) >, < ( 1 / 4 ) C, ( 1 / 2 ) >, < ( 1 / 4 ) D, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }
    
    /**
     * Test that rests in a tuplet are multiplied by 2/3
     */
    @Test
    public void testRestsInTuplet(){
        Piece piece = new Piece();
        piece.setDefaultNoteLength(new Fraction(1, 4));
        piece.setMeter(new Fraction(4, 4));
        HashMap<String, Pitch> scale = CircleOfFifths.getKeySignature("G");

        List<Token> measure = Parser.lex("A (3AzD D|");
        ListIterator<Token> iter = measure.listIterator();
        Measure m = new Measure();
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 4 ) A, ( 0 / 1 ) >, < ( 1 / 6 ) A, ( 1 / 4 ) >, " +
        		"< ( 1 / 6 ) D, ( 7 / 12 ) >, < ( 1 / 4 ) D, ( 3 / 4 ) >]";
        assertEquals(expected, m.toString());
    }

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
                new Note(new Fraction(1, 2), new Pitch('C').transpose(36)),
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
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
        Measure m = new Measure();
        Parser.parseMeasureContents(piece, m, iter, scale);
        String expected = "[< ( 1 / 6 ) A, ( 0 / 1 ) >, < ( 1 / 6 ) B, ( 1 / 6 ) >, < ( 1 / 6 ) C, ( 1 / 3 ) >, " +
        		"< ( 1 / 32 ) C, ( 1 / 2 ) >, < ( 1 / 4 ) d', ( 1 / 2 ) >, < ( 1 / 12 ) ^F, ( 1 / 2 ) >, " +
        		"< ( 1 / 32 ) F,, ( 1 / 2 ) >]";
        assertEquals(expected, m.toString());
    }

}
