package utilities;

/**
 * Fraction Instances are immutable objects. They are guaranteed to be in least
 * terms by structural induction. If the fraction is zero ( 0 ), least terms is
 * defined as ( 0/1 ). An exception will be thrown if a fraction of the
 * undefined form ( n/0 ) is ever attempted.
 * 
 * @author woursler
 */
public class Fraction {

	private final int numerator;
	private final int denominator;

	// Static helper methods...
	// TODO: Implement and doc
	public static int gcd(int first, int second) {
		return 0;
	}

	// TODO: Implement and doc
	public static int lcm(int first, int second) {
		return 0;
	}

	public Fraction(int value) {
		this.numerator = value;
		this.denominator = 1;
	}

	public Fraction(int numerator, int denominator) {
		// Handle a special case...
		if (numerator == 0) {
			this.numerator = 0;
			this.denominator = 1;
			return;
		}
		// TODO: Handle negative numbers....
		int gcd = 1; // TODO fix;
		this.numerator = numerator / gcd;
		this.denominator = denominator / gcd;
	}

	public Fraction plus(Fraction other) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the current fraction to ( other / 1 ).
	 * 
	 * @param other
	 *            an int to to add.
	 * @return The result of the addition.
	 */
	public Fraction plus(int other) {
		return this.plus(new Fraction(other));
	}

	public Fraction times(Fraction offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public float approximation() {
		return this.numerator / ((float) this.denominator);
	}

}