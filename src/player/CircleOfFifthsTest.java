package player;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for CircleOfFifths
 * @author kimtoy
 * @category no_didit
 *
 */
public class CircleOfFifthsTest {
    @Test
    public void testMajorKeySignatures(){
        assertArrayEquals(CircleOfFifths.getKeySignature("C"), new String[]{});
        assertArrayEquals(CircleOfFifths.getKeySignature("G"), new String[]{"F#"});
        assertArrayEquals(CircleOfFifths.getKeySignature("E"), new String[]{"F#","C#","G#","D#"});
        assertArrayEquals(CircleOfFifths.getKeySignature("Eb"), new String[]{"Bb","Eb","Ab"});
        assertArrayEquals(CircleOfFifths.getKeySignature("Cb"), new String[]{"Bb","Eb","Ab","Db","Gb","Cb","Fb"});
    }
    
    @Test
    public void testMinorKeySignatures(){
        assertArrayEquals(CircleOfFifths.getKeySignature("am"), new String[]{});
        assertArrayEquals(CircleOfFifths.getKeySignature("em"), new String[]{"F#"});
        assertArrayEquals(CircleOfFifths.getKeySignature("c#m"), new String[]{"F#","C#","G#","D#"});
        assertArrayEquals(CircleOfFifths.getKeySignature("cm"), new String[]{"Bb","Eb","Ab"});
        assertArrayEquals(CircleOfFifths.getKeySignature("abm"), new String[]{"Bb","Eb","Ab","Db","Gb","Cb","Fb"});
    }
    
    @Test
    public void testIgnoreCase(){
        assertArrayEquals(CircleOfFifths.getKeySignature("c"), new String[]{});
    }
}
