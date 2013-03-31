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
        assertEquals(Fraction.gcd(1, 2), 1);
    }
    
    /**
     * Check that the Fraction constructor correctly reduces the fraction
     */
    @Test 
    public void testConstructorReduce(){
        Fraction f = new Fraction(1,2);
        assertEquals(1, f.numerator);
        assertEquals(2, f.denominator);
    }
}
