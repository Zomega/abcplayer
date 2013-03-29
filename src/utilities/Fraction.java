package utilities;

//TODO: Proofread / sync documentation.
//TODO: FractionTest may be in order.
/**
 * Fraction Instances are immutable objects. They are guaranteed to be in least
 * terms by structural induction. If the fraction is zero ( 0 ), least terms is
 * defined as ( 0/1 ). An exception will be thrown if a fraction of the
 * undefined form ( n/0 ) is ever attempted, or if the fraction has a negative
 * numerator or denominator (in either simplified or unsimplified form).
 * Invariant: the fraction is always in lowest terms.
 * 
 * @author woursler and czuo
 * @version beta
 */
public class Fraction {

	/**
	 * The fraction's numerator. May take on any value, including negative ones.
	 */
	public final int numerator;
	/**
	 * The fraction's denominator. Must be a strictly positive integer.
	 */
	public final int denominator;

	// Static helper methods...
	/**
	 * Method which finds and returns the GCD of two nonzero integers by
	 * implementing Euclid's algorithm.
	 * 
	 * @param first - an integer
	 * @param second - an integer
	 * @return their greatest common divisor
	 */
	public static int gcd(int first, int second) {
	    if(first>second){
	        if (second == 0) {
                return first;
            }
            return gcd(second, first % second);
	    }
	    else{
	        if (first == 0) {
                return second;
            }
            return gcd(first, second % first);
	    }
	}

	/**
	 * Method which finds and returns the LCM of two nonzero integers
	 * 
	 * @param first - an integer
	 * @param second - an integer
	 * @return their least common multiple
	 */
	public static int lcm(int first, int second) {
		return (first * second) / gcd(first, second);
	}

	// Other Methods.
	/**
	 * Constructor for a Fraction with a single parameter, int value. The
	 * Fraction is equal to (value / 1); thus in lowest terms.
	 * 
	 * @param value
	 *            , the integer value of the fraction.
	 */
	public Fraction(int value) {
		this.numerator = value;
		this.denominator = 1;
	}

	/**
	 * Constructor for a Fraction with two parameters, numerator and
	 * denominator. The Fraction constructed is in lowest terms.
	 * 
	 * @param int numerator, int denominator.
	 * @throws FractionValueException
	 *             if denominator = 0, or if either is negative.
	 */
	public Fraction(int numerator, int denominator)
			throws FractionValueException {

		// Handle a special case where we've been given something invalid.
		if (denominator == 0) {
			throw new FractionValueException("Zero in denominator");
		}

		// Handle the special case where we have a zero fraction.
		if (numerator == 0) {
			this.numerator = 0;
			this.denominator = 1;
			return;
		}

		// Store the final sign...
		boolean isPositive = (numerator * denominator > 0);

		numerator = Math.abs(numerator);
		denominator = Math.abs(denominator);

		int gcd = gcd(numerator, denominator);
		// Add back in our sign.
		if (isPositive)
			this.numerator = numerator / gcd;
		else
			this.numerator = -1 * numerator / gcd;
		this.denominator = denominator / gcd;
	}

	/**
	 * Returns the sum of the current fraction with the other fraction. Note
	 * that other can never be invalid by construction thus the returned value
	 * can never be invalid.
	 * 
	 * @param Fraction
	 *            other
	 * @return new Fraction representing the sum of this and other
	 */
	public Fraction plus(Fraction other) {
		try {
			return new Fraction(this.numerator * other.denominator
					+ other.numerator * this.denominator, this.numerator
					* other.numerator);
		} catch (Exception e) {
			// This shouldn't happen.
			return null;
		}
	}

	/**
	 * Returns the sum of the current fraction with ( other / 1 ).
	 * 
	 * @param other
	 *            an int to to add.
	 * @return The result of the addition.
	 */
	public Fraction plus(int other) {
		return this.plus(new Fraction(other));
	}

	/**
	 * Returns the difference of the current fraction with the other fraction.
	 * Note that other can never be invalid by construction thus the returned
	 * value can never be invalid.
	 * 
	 * @param Fraction
	 *            other
	 * @return new Fraction representing the sum of this and other
	 */
	public Fraction minus(Fraction other) {
		return this.plus(other.times(-1));
	}

	/**
	 * Returns the difference of the current fraction with ( other / 1 ).
	 * 
	 * @param other
	 *            an int to to add.
	 * @return The result of the addition.
	 */
	public Fraction minus(int other) {
		return this.plus(-1 * other);
	}

	/**
	 * Returns the product of the current fraction and the other fraction. Note
	 * that other can never invalid by construction thus the returned value can
	 * never be invalid.
	 * 
	 * @param other
	 *            the other Fraction.
	 * @return new Fraction representing the product of this and other
	 */
	public Fraction times(Fraction other) {
		return new Fraction(this.numerator * other.numerator, this.denominator
				* other.denominator);
	}

	/**
	 * Returns the product of the current fraction and ( other / 1 ).
	 * 
	 * @param other
	 *            an int to to multiply by.
	 * @return The result of the multiplication.
	 */
	public Fraction times(int other) {
		return this.times(new Fraction(other));
	}

	/**
	 * Returns the quotient of the fractions.
	 * 
	 * @param other
	 *            a Fraction to to divide by.
	 * @return The result of the division.
	 */
	public Fraction quotient(Fraction other) {
		return new Fraction(this.numerator * other.denominator,
				this.denominator * other.numerator);
	}

	/**
	 * Returns the quotient of the current fraction and ( other / 1 ).
	 * 
	 * @param other
	 *            an int to to divide by.
	 * @return The result of the division.
	 */
	public Fraction quotient(int other) {
		return new Fraction(this.numerator, this.denominator * other);
	}

	/**
	 * Takes the inverse of this fraction.
	 * 
	 * @return a new Fraction, the inverse of this Fraction.
	 * @throws FractionValueException
	 *             if this fraction is zero.
	 */
	public Fraction inverse() throws FractionValueException {
		return new Fraction(this.denominator, this.numerator);
	}

	/**
	 * @return a float approximation of the numerical value of this.
	 */
	public float approximation() {
		return this.numerator / ((float) this.denominator);
	}

	public int hashCode() {
		return (this.numerator + this.denominator) * this.denominator
				+ this.numerator;
	}

	public boolean equals(Object other) {
		if (other instanceof Fraction) {
			Fraction o = (Fraction) other;
			return ((this.numerator == o.numerator) && (this.denominator == o.denominator));
		}
		return false;
	}

	public String toString() {
		return "( " + this.numerator + " / " + this.denominator + " )";
	}
}