package lexer;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;

public class LexerMusicTest {
    public final TokenType FIELD_NUM = new TokenType("FIELD_NUM",
            Pattern.compile("X:"));
    public final  TokenType FIELD_TITLE = new TokenType("FIELD_TITLE",
            Pattern.compile("T:.*\\n"));
    public final  TokenType FIELD_COMP = new TokenType("FIELD_COMP",
            Pattern.compile("C:.*\\n"));
    public final  TokenType FIELD_DEFAULT_LEN = new TokenType("FIELD_DEFAULT_LEN",
            Pattern.compile("L:\\d+/\\d+\\n"));
    public final  TokenType FIELD_METER = new TokenType("FIELD_METER",
            Pattern.compile("M:"));
    public final  TokenType FIELD_TEMPO = new TokenType("FIELD_TEMPO",
            Pattern.compile("Q:"));
    public final TokenType FIELD_VOICE = new TokenType("FIELD_VOICE",
            Pattern.compile("V:.*\\n"));
    public final  TokenType FIELD_KEY = new TokenType("FIELD_KEY",
            Pattern.compile("K:"));
    public final  TokenType BASENOTE = new TokenType("BASENOTE",
            Pattern.compile("[a-gA-G]{1}"));
    public final  TokenType KEY_ACCIDENTAL = new TokenType("KEY_ACCIDENTAL",
            Pattern.compile("[#b]+"));
    public final  TokenType ACCIDENTAL = new TokenType("ACCIDENTAL",
            Pattern.compile("(\\^)|(\\^\\^)|(_)|(__)|(=)"));
    public final  TokenType MODE_MINOR = new TokenType("MODE_MINOR",
            Pattern.compile("m"));
    public final  TokenType METER = new TokenType("METER",
            Pattern.compile("(C)|(C\\|)|(\\d+/\\d+)"));
    public final  TokenType OCTAVE = new TokenType("OCTAVE",
            Pattern.compile("('+)|(,+)"));
    public final  TokenType DUPLET = new TokenType("DUPLET",
            Pattern.compile("\\(2"));
    public final  TokenType TUPLET = new TokenType("TUPLET",
            Pattern.compile("\\(3"));
    public final  TokenType QUADRUPLET = new TokenType("QUADRUPLET",
            Pattern.compile("\\(4"));
    public final  TokenType OPEN_REPEAT = new TokenType("OPEN_REPEAT",
            Pattern.compile("\\|:"));
    public final  TokenType CLOSE_REPEAT = new TokenType("CLOSE_REPEAT",
            Pattern.compile(":\\|"));
    public final  TokenType DOUBLE_BARLINE = new TokenType("DOUBLE_BARLINE",
            Pattern.compile("(\\|\\|)|(\\[\\|)|(\\|\\])"));
    public final  TokenType BARLINE = new TokenType("BARLINE",
            Pattern.compile("\\|"));
    public final  TokenType ONE_REPEAT = new TokenType("ONE_REPEAT",
            Pattern.compile("\\[1"));
    public final  TokenType TWO_REPEAT = new TokenType("TWO_REPEAT",
            Pattern.compile("\\[2"));
    public final TokenType FRACTION = new TokenType("FRACTION",
            Pattern.compile("\\d+/\\d+"));
    public final TokenType FRACTION_NOT_STRICT = new TokenType("FRACTION_NOT_STRICT",
            Pattern.compile("\\d*/\\d*"));
    public final  TokenType DIGITS = new TokenType("DIGITS",
            Pattern.compile("\\d+"));
    public final  TokenType REST = new TokenType("REST",
            Pattern.compile("z"));
    public final  TokenType OPEN_CHORD = new TokenType("OPEN_CHORD",
            Pattern.compile("\\["));
    public final  TokenType CLOSE_CHORD = new TokenType("CLOSE_CHORD",
            Pattern.compile("\\]"));
    public final  TokenType COMMENT = new TokenType("COMMENT",
            Pattern.compile("%.*\\n"));
    public final  TokenType NEWLINE = new TokenType("NEWLINE",
            Pattern.compile("\\n"));
    public final  TokenType SPACE = new TokenType("SPACE",
            Pattern.compile("[\\s]+"));
    
    List<TokenType> types = new ArrayList<TokenType>();

    @Before
    public void setUp() {
        types.add(FIELD_NUM);
        types.add(FIELD_TITLE);
        types.add(FIELD_COMP);
        types.add(FIELD_DEFAULT_LEN);
        types.add(FIELD_METER);
        types.add(FIELD_TEMPO);
        types.add(FIELD_VOICE);
        types.add(FIELD_KEY);
        types.add(BASENOTE);
        types.add(KEY_ACCIDENTAL);
        types.add(ACCIDENTAL);
        types.add(MODE_MINOR);
        types.add(METER);
        types.add(OCTAVE);
        types.add(DUPLET);
        types.add(TUPLET);
        types.add(QUADRUPLET);
        types.add(OPEN_REPEAT);
        types.add(CLOSE_REPEAT);
        types.add(DOUBLE_BARLINE);
        types.add(BARLINE);
        types.add(ONE_REPEAT);
        types.add(TWO_REPEAT);
        types.add(FRACTION);
        types.add(FRACTION_NOT_STRICT);
        types.add(DIGITS);
        types.add(REST);
        types.add(OPEN_CHORD);
        types.add(CLOSE_CHORD);
        types.add(COMMENT);
        types.add(NEWLINE);
        types.add(SPACE);
    }
    
    @Test
    public void test1() {
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
        //System.out.println(line);
        Lexer l = new Lexer(types);
        List<Token> tokens = l.lex(line);
        for(int i=0; i<tokens.size(); i++){
            System.out.println(tokens.get(i).toString());
        }
    }
}
