package player;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

import sound.Pitch;

/**
 * Test class for CircleOfFifths
 * 
 * @author kimtoy
 * @category no_didit
 * 
 */
public class CircleOfFifthsTest {
    HashMap<String, Pitch> CM;//same as am
    HashMap<String, Pitch> CSharpM;//same as a#m
    HashMap<String, Pitch> CFlatM;//same as abm
    
    String[] keys = { "C","D","E","F","G","A","B" };
    Pitch[] CMamPitch = { new Pitch('C'), new Pitch('D'), new Pitch('E'),
            new Pitch('F'), new Pitch('G'), new Pitch('A'), new Pitch('B') };
    Pitch[] CSasmPitch = { new Pitch('C').transpose(1),
            new Pitch('D').transpose(1), new Pitch('E').transpose(1),
            new Pitch('F').transpose(1), new Pitch('G').transpose(1),
            new Pitch('A').transpose(1), new Pitch('B').transpose(1) };
    Pitch[] CFafmPitch = { new Pitch('C').transpose(-1),
            new Pitch('D').transpose(-1), new Pitch('E').transpose(-1),
            new Pitch('F').transpose(-1), new Pitch('G').transpose(-1),
            new Pitch('A').transpose(-1), new Pitch('B').transpose(-1) };

    @Before
    public void setup(){
        CM = new HashMap<String, Pitch>();
        CSharpM = new HashMap<String, Pitch>();
        CFlatM = new HashMap<String, Pitch>();
        mapInitializer(CM, keys, CMamPitch);
        mapInitializer(CSharpM, keys, CSasmPitch);
        mapInitializer(CFlatM, keys, CFafmPitch);
    }

    /**
     * Helper method to populate hashmap quickly. Array s must be the same length as p
     * 
     * @param map
     * @param s
     * @param p
     */
    public void mapInitializer(HashMap<String, Pitch> map, String[] s, Pitch[] p) {
        for(int i=0; i<s.length; i++){
            map.put(s[i], p[i]);
        }
    }
    
    @Test
    public void testInitializer(){
        System.out.println(CircleOfFifths.getKeySignature("G"));
        System.out.println(CircleOfFifths.getKeySignature("B"));
        System.out.println(CircleOfFifths.getKeySignature("dm"));
        System.out.println(CircleOfFifths.getKeySignature("Eb"));
        System.out.println(CircleOfFifths.getKeySignature("c#m"));
        System.out.println(CircleOfFifths.getKeySignature("f#m"));
        
        System.out.println(CM.get("C").transpose(1));
        System.out.println(CM.get("C"));
    }
    
    @Test
    public void testNoAccidentalSignatures(){
        assertEquals(CircleOfFifths.getKeySignature("C"), CM);
        assertEquals(CircleOfFifths.getKeySignature("am"), CM);
    }
    
    @Test 
    public void testMajorSharps(){
        assertEquals(CircleOfFifths.getKeySignature("C#"), CSharpM);
    }
    
    @Test
    public void testMinorSharps(){
        assertEquals(CircleOfFifths.getKeySignature("a#m"), CSharpM);
    }
    
    @Test 
    public void testMajorFlats(){
        assertEquals(CircleOfFifths.getKeySignature("Cb"), CFlatM);
    }
    
    @Test
    public void testMinorFlats(){
        assertEquals(CircleOfFifths.getKeySignature("abm"), CFlatM);
    }
    
    @Test
    public void ignoresCase(){
        assertEquals(CircleOfFifths.getKeySignature("c"), CM);
        assertEquals(CircleOfFifths.getKeySignature("AM"), CM);
    }
}
