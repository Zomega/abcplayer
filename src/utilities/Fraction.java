package utilities;

/**
 * Fraction Instances are immutable objects. They are guaranteed to be in least
 * terms by structural induction. If the fraction is zero ( 0 ), least terms is
 * defined as ( 0/1 ). An exception will be thrown if a fraction of the
 * undefined form ( n/0 ) is ever attempted, or if the fraction has a negative
 * numerator or denominator (in either simplified or unsimplified form).  
 * Invariant: the fraction is always in lowest terms.  
 * 
 * @author woursler
 */
public class Fraction {

	private final int numerator;
	private final int denominator;

	// Static helper methods...
	// TODO: Implement and doc
	
	public int getNum()
	{
	    return this.numerator;
	}
	public int getDenom()
	{
	    return this.denominator;
	}
	/**
	 * Method which finds and returns the GCD of two nonzero integers
	 * by implementing Euclid's algorithm.
	 * @param first - a nonzero integer
	 * @param second - a nonzero integer
	 * @return their greatest common divisor
	 */
	public static int gcd(int first, int second) {
		if (second == 0)
		{
		    return 0;
		}
		return gcd(second, first % second);
	}

    /**
     * Method which finds and returns the LCM of two nonzero integers
     * @param first - a nonzero integer
     * @param second - a nonzero integer
     * @return their least common multiple
     */
	public static int lcm(int first, int second) {
	    return (first*second)/gcd(first, second);
	}

	/**
	 * Constructor for a Fraction with a single parameter, int value.
	 * The Fraction is equal to (value / 1); thus in lowest terms.
	 * @param value, the nonnegative integer value of the fraction.  
	 * @throws BadFractionException if value is negative
	 */
	public Fraction(int value) throws BadFractionException{
	    if (value < 0)
	    {
	        throw new BadFractionException("Negative number given as numerator");
	    }
		this.numerator = value;
		this.denominator = 1;
	}

	 /**
     * Constructor for a Fraction with two parameters, numerator and denominator.
     * The Fraction constructed is in lowest terms.  
     * @param int numerator, int denominator.
     * @throws BadFractionException if denominator = 0, or if either is negative. 
     */
	public Fraction(int numerator, int denominator) throws BadFractionException {
		// Handle a special case.
	    if (denominator == 0)
	    {
	        throw new BadFractionException("Zero in denominator");
	    }
	    else if ((denominator < 0) || (numerator < 0))
	    {
	        throw new BadFractionException("Negative numbers in num or denom");
	    }
	    //This ensures that our fractions are nonnegative.  Therefore, in plus
	    //and times, as long as we aren't adding/multiplying by negative integers,
	    //we should always have a nonnegative fraction.  This is our invariant.  
	    else if (numerator == 0) {
			this.numerator = 0;
			this.denominator = 1;
			return;
		}

		int gcd = gcd(numerator, denominator); 
		this.numerator = numerator / gcd;
		this.denominator = denominator / gcd;
	}

	/**
	 * Returns the sum of the current fraction to the other fraction.
	 * Note that other can never be negative or invalid by construction
	 * thus the returned value can never be negative or invalid.
	 * @param Fraction other
	 * @return new Fraction representing the sum of this and other
	 * @throws BadFractionException
	 */
	public Fraction plus(Fraction other) throws BadFractionException {
		int newnum = this.numerator * other.getDenom() + other.getNum() * this.denominator;
		int newdenom = this.denominator * other.getDenom();
		int newgcd = gcd(newnum, newdenom);
		return new Fraction(newnum / newgcd, newdenom / newgcd);
		
	}

	/**
	 * Returns the sum of the current fraction to ( other / 1 ).
	 * Note that if other is negative, then in constructing
	 * new Fraction(other) the BadFractionException will be thrown.  
	 * @param other
	 *            an int to to add.
	 * @return The result of the addition.
	 * @throws BadFractionException
	 */
	public Fraction plus(int other) throws BadFractionException {
		return this.plus(new Fraction(other));
	}

	 /**
     * Returns the product of the current fraction to the other fraction.
     * Note that other can never be negative or invalid by construction
     * thus the returned value can never be negative or invalid.
     * @param Fraction other
     * @return new Fraction representing the product of this and other
     * @throws BadFractionException
     */
	public Fraction times(Fraction offset) throws BadFractionException {
	      int newnum = this.numerator * offset.getNum();
	      int newdenom = this.denominator * offset.getDenom();
	      int newgcd = gcd(newnum, newdenom);
	      return new Fraction(newnum / newgcd, newdenom / newgcd);
	}
	
	 /**
     * Returns the product of the current fraction to ( other / 1 ).
     * Note that if other is negative, then in constructing
     * new Fraction(other) the BadFractionException will be thrown.  
     * @param other
     *            an int to to multiply.
     * @return The result of the multiplication.
     * @throws BadFractionException
     */
	public Fraction times(int offset) throws BadFractionException {
	    return this.times(new Fraction(offset));
	}

	/**
	 * Takes the inverse of this.
	 * @return a new Fraction, the inverse of this Fraction.
	 * @throws BadFractionException
	 */
	public Fraction invert() throws BadFractionException {
	    return new Fraction(this.denominator, this.numerator);
	}
	
	public float approximation() {
		return this.numerator / ((float) this.denominator);
	}

}