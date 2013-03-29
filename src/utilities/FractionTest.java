package utilities;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Fraction test file
 * 
 * @author kimtoy
 * @category no_didit
 */
public class FractionTest {
    @Test
    public void testGCD(){
        assertEquals(Fraction.gcd(1, 5), 1);
        assertEquals(Fraction.gcd(5, 1), 1);
        assertEquals(Fraction.gcd(0, 10), 10);
        assertEquals(Fraction.gcd(9, 27), 9);
        assertEquals(Fraction.gcd(42, 35), 7);
    }
}
