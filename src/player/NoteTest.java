package player;

import static org.junit.Assert.*;

import org.junit.Test;

import sound.Pitch;
import utilities.Fraction;

/**
 * Test equals method of Note
 * @author kimtoy
 *
 */
public class NoteTest {
    
    @Test
    public void testEquals(){
        assertEquals(new Note(new Fraction(1,2), new Pitch('C')), new Note(new Fraction(1,2), new Pitch('C')));
        Note n = new Note(new Fraction(2,3), new Pitch('D'));
        assertEquals(n, n);//Test that note equals itself
    }

}
