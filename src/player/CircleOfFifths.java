package player;

import java.util.HashMap;

import sound.Pitch;

/**
 * This class takes a basenote and accidental, and returns the eight scale notes
 * corresponding to that key.
 * 
 * All String comparisons are made case insensitive, although there may be
 * problems with dependent classes which do not ignore case.
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
    public static final String[] NONSHARPS = {"F","C","G","D","A","E","B"};
    public static final String[] NONFLATS = {"B","E","A","D","G","C","F"};
    
    /**
     * Returns a HashMap of the scale for the given key with the appropriate key signature 
     * 
     * @param key must be a valid key non-terminal according to the defined abc grammar.
     * @return A HashMap relating each basenote to its appropriate pitch given the key signature
     */
    public static HashMap<String, Pitch> getKeySignature(String key){
        HashMap<String, Pitch> scale = new HashMap<String, Pitch>();
        
        for(int i=0; i<FIFTHS_MAJOR_SHARP.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MAJOR_SHARP[i])){
                for(int j=0; j<NONSHARPS.length; j++){
                    if(j<i){
                        scale.put(NONSHARPS[j], new Pitch(NONSHARPS[j].charAt(0)).transpose(1));
                    }
                    else
                        scale.put(NONSHARPS[j], new Pitch(NONSHARPS[j].charAt(0)));
                }
                return scale;
            }
        }
        for(int i=0; i<FIFTHS_MAJOR_FLAT.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MAJOR_FLAT[i])){
                for(int j=0; j<NONFLATS.length; j++){
                    if(j<i){
                        scale.put(NONFLATS[j], new Pitch(NONFLATS[j].charAt(0)).transpose(-1));
                    }
                    else
                        scale.put(NONFLATS[j], new Pitch(NONFLATS[j].charAt(0)));
                }
                return scale;
            }
        }
        for(int i=0; i<FIFTHS_MINOR_SHARP.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MINOR_SHARP[i])){
                for(int j=0; j<NONSHARPS.length; j++){
                    if(j<i){
                        scale.put(NONSHARPS[j], new Pitch(NONSHARPS[j].charAt(0)).transpose(1));
                    }
                    else
                        scale.put(NONSHARPS[j], new Pitch(NONSHARPS[j].charAt(0)));
                }
                return scale;
            }
        }
        for(int i=0; i<FIFTHS_MINOR_FLAT.length; i++){
            if(key.equalsIgnoreCase(FIFTHS_MINOR_FLAT[i])){
                for(int j=0; j<NONFLATS.length; j++){
                    if(j<i){
                        scale.put(NONFLATS[j], new Pitch(NONFLATS[j].charAt(0)).transpose(-1));
                    }
                    else
                        scale.put(NONFLATS[j], new Pitch(NONFLATS[j].charAt(0)));
                }
                return scale;
            }
        }
        return null;
    }
}
