package utilities;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Fraction test file
 * 
 * Testing strategy:
 * -Comprehensive tests that check each method and constructor
 * -Check that exceptions are thrown when expected
 * Individual component strategies are explained
 * 
 * @author kimtoy
 * @category no_didit
 */

public class FractionTest {
    
    /**
     * Check that the Fraction greatest common factor function correctly returns the greatest common denominator of two values
     * It should:
     * -correctly return the same value if the parameters are switched
     * -return 0 if one of the numbers is 0
     * -return 1 if one of the numbers is 1
     * -return n if asked for the gcf of n and n
     * -return positive values when given negative paramters
     */
    @Test
    public void testGCD(){
        assertEquals(Fraction.gcd(1, 5), 1);
        assertEquals(Fraction.gcd(5, 1), 1);
        assertEquals(Fraction.gcd(0, 10), 10);
        assertEquals(Fraction.gcd(100, 100), 100);
        assertEquals(Fraction.gcd(9, 27), 9);
        assertEquals(Fraction.gcd(42, 35), 7);
        assertEquals(Fraction.gcd(-1, 2), 1);
        assertEquals(Fraction.gcd(-12, -4), 4);
    }
    
    /**
     * Check that the Fraction constructor correctly reduces the fraction
     * Should:
     * -Have correct numerator/denominator if fraction cannot be reduced further
     * -Correctly reduce any reducable fraction
     * -Behave correctly with zero value fraction
     * -Keep the correct sign
     * -Work correctly with a fraction>1
     */
    @Test 
    public void testConstructorReduce(){
        Fraction f = new Fraction(1,2);
        assertEquals(1, f.numerator);
        assertEquals(2, f.denominator);
        
        Fraction f2 = new Fraction(7,21);
        assertEquals(1, f2.numerator);
        assertEquals(3, f2.denominator);
        
        Fraction f3 = new Fraction(0,-3);//does not matter whether denominator is pos/neg
        assertEquals(0, f3.numerator);
        assertEquals(1, f3.denominator);
        
        Fraction f4 = new Fraction(-5, 10);
        assertEquals(-1, f4.numerator);
        assertEquals(2, f4.denominator);
        
        Fraction f5 = new Fraction(-5, -10);
        assertEquals(1, f5.numerator);
        assertEquals(2, f5.denominator);
        
        Fraction f6 = new Fraction(10, 2);
        assertEquals(5, f6.numerator);
        assertEquals(1, f6.denominator);
    }

    /**
     * Test that none of the fraction methods mutate Fraction objects
     */
    @Test
    public void testImmutability(){
        Fraction f = new Fraction(3,4);
        Fraction g = new Fraction(12,5);
        f.minus(g);
        f.minus(3);
        f.plus(g);
        f.plus(5);
        f.inverse();
        f.quotient(f);
        f.quotient(2);
        f.times(f);
        f.times(5);
        assertEquals(new Fraction(3,4), f);
        assertEquals(new Fraction(12,5), g);
    }
    
    /**
     * Test that equals() method correctly returns a value for:
     * -two Fraction objects that have the same numerator and denominator
     * -two fractions that reduce to the same number
     * -two fractions that have the same sign and reduction, but one has a numerator<0, and the other has a denominator<0
     * -a fraction compared to itself
     * -two fractions that are unequal in sign
     * -two fractions that do not reduce to the same fraction
     */
    @Test
    public void testEquals(){
        assertEquals(new Fraction(2,3), new Fraction(2,3));
        assertEquals(new Fraction(7,14), new Fraction(1,2));
        assertEquals(new Fraction(-3,6), new Fraction(1,-2));
        Fraction f = new Fraction(6, 9);
        assertEquals(f,f);
        assertFalse(new Fraction(7,2).equals(new Fraction(3,4)));
        assertFalse(new Fraction(-3,4).equals(new Fraction(3,4)));
    }
    
    /**
     * Test that error is thrown if Fraction is attempted to be initialized with a zero denominator
     */
    @Test(expected=FractionValueException.class)
    public void testDivideByZeroInitialization(){
        new Fraction(2,0);
    }
    
    /**
     * Test that an error is thrown if zero valued Fraction is inverted
     */
    @Test(expected=FractionValueException.class)
    public void testInverseException(){
        new Fraction(0,1).inverse();
    }
    
    /**
     * Tests plus(Fraction other) and plus(int other) methods
     * -Should correctly add fractions with different denominators
     * -Correctly add negative fractions
     */
    @Test
    public void testPlus(){
        assertEquals(new Fraction(3,4), new Fraction(1,4).plus(new Fraction(1,2)));
        assertEquals(new Fraction(19,10), new Fraction(9,10).plus(1));
        assertEquals(new Fraction(-3,4), new Fraction(-1,4).plus(new Fraction(-1,2)));
        assertEquals(new Fraction(-1,10), new Fraction(9,10).plus(-1));
    }
}
