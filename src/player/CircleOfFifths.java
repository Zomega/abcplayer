package player;

import java.util.Arrays;

/**
 * This class takes a basenote and accidental, and returns the eight scale notes corresponding to that key.  
 * 
 * All String comparisons are made case insensitive, although there may be problems with dependent classes
 * which do not ignore case.  
 * 
 * @author kimtoy
 *
 */
public class CircleOfFifths {
    public static final String[] FIFTHS_MAJOR_SHARP = {"C","G","D","A","E","B","F#","C#"};
    public static final String[] FIFTHS_MAJOR_FLAT= {"C","F","Bb","Eb","Ab","Db","Gb","Cb"};
    public static final String[] FIFTHS_MINOR_SHARP = {"am","em","bm","f#m","c#m","g#m","d#m","a#m"};
    public static final String[] FIFTHS_MINOR_FLAT = {"am","dm","gm","cm","fm","bbm","ebm","abm"};
    public static final String[] SHARPS = {"F#","C#","G#","D#","A#","E#","B#"};
    public static final String[] FLATS = {"Bb","Eb","Ab","Db","Gb","Cb","Fb"};
    
    /**
     * @param key must be a valid key non-terminal according to the defined abc grammar.
     * @return A List of the appropriate accidentals in String form.
     */
    public static String[] getKeySignature(String key){
        for(int i=0; i<FIFTHS_MAJOR_SHARP.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MAJOR_SHARP[i])){
                return Arrays.copyOfRange(SHARPS, 0, i);
            }
        }
        for(int i=0; i<FIFTHS_MAJOR_FLAT.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MAJOR_FLAT[i])){
                return Arrays.copyOfRange(FLATS, 0, i);
            }
        }
        for(int i=0; i<FIFTHS_MINOR_SHARP.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MINOR_SHARP[i])){
                return Arrays.copyOfRange(SHARPS, 0, i);
            }
        }
        for(int i=0; i<FIFTHS_MINOR_FLAT.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MINOR_FLAT[i])){
                return Arrays.copyOfRange(FLATS, 0, i);
            }
        }
        return null;
    }
}
