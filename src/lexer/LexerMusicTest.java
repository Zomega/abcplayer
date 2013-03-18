package lexer;

import java.util.regex.Pattern;

public class LexerMusicTest {
    public final TokenType FIELD_NUM = new TokenType("FILED_NUM",
            Pattern.compile("X:"));
    public final  TokenType FIELD_TITLE = new TokenType("FIELD_TITLE",
            Pattern.compile("T:"));
    public final  TokenType FIELD_COMP = new TokenType("FIELD_COMP",
            Pattern.compile("C:"));
    public final  TokenType FIELD_DEFAULT_LEN = new TokenType("FIELD_DEFAULT_LEN",
            Pattern.compile("L:"));
    public final  TokenType FIELD_METER = new TokenType("FIELD_METER",
            Pattern.compile("M:"));
    public final  TokenType FIELD_TEMPO = new TokenType("FIELD_TEMPO",
            Pattern.compile("Q:"));
    public final TokenType FIELD_VOICE = new TokenType("FIELD_VOICE",
            Pattern.compile("V:"));
    public final  TokenType FIELD_KEY = new TokenType("FIELD_KEY",
            Pattern.compile("K:"));
    public final  TokenType KEYNOTE = new TokenType("KEYNOTE",
            Pattern.compile("[a-zA-Z]+"));
    public final  TokenType KEY_ACCIDENTAL = new TokenType("KEY_ACCIDENTAL",
            Pattern.compile("[*+]"));
    public final  TokenType MODE_MINOR = new TokenType("MODE_MINOR",
            Pattern.compile("\\("));
    public final  TokenType METER = new TokenType("METER",
            Pattern.compile("C | C\\| |"));
    public final TokenType TEMPO = new TokenType("TEMPO",
            Pattern.compile("\\d+(?!\\.)"));
    public final  TokenType NOTE = new TokenType("NOTE",
            Pattern.compile("(\\d*\\.\\d+(?!\\.))|(\\d+\\.\\d*(?!\\.))"));
    public final  TokenType TUPLET = new TokenType("TUPLET",
            Pattern.compile("[a-zA-Z]+"));
    public final  TokenType BARLINE = new TokenType("BARLINE",
            Pattern.compile("[*+]"));
    public final  TokenType NTH_REPEAT = new TokenType("NTH_REPEAT",
            Pattern.compile("\\("));
    public final  TokenType MID_TUNE_FIELD = new TokenType("MID_TUNE_FIELD",
            Pattern.compile("\\)"));
    public final TokenType FRACTION = new TokenType("FRACTION",
            Pattern.compile("\\d+/\\d+"));
    public final  TokenType REST = new TokenType("REST",
            Pattern.compile("z"));
    public final  TokenType CHORD = new TokenType("CHORD",
            Pattern.compile("\\[\\]"));
    public final  TokenType NEWLINE = new TokenType("NEWLINE",
            Pattern.compile("\\n)"));
    public final  TokenType SPACE = new TokenType("SPACE",
            Pattern.compile("[\\t\\x0B\\f\\r]+"));
}
