package player;

import utilities.Fraction;

public class FractionParser {

	//TODO: It is possible that this should be a static member of Fraction.
	/**
	 * @param fract A string with no whitespace that can be parsed as a Fraction.
	 * @return The parsed fraction object.
	 */
    public static Fraction parseFraction(String fract)
    {
        int cutoff = fract.indexOf('/');
        if (cutoff == -1)
        {
            return new Fraction(Integer.parseInt(fract));
        }
        
        String num = fract.substring(0, cutoff);
        String denom = fract.substring(cutoff);  
        //include '/' in denom to avoid IndexExceptions from substring method
        
        //Consider making these constructor parameters for private final int fields
        int first = 1;  //default for numerator
        int second = 2;  //default for denominator
        
        
        if (num.length() != 0)
        {
            first = Integer.parseInt(num);
        }
        if ((denom != "/") && (denom.length() > 1))  
            //denom should not be "" ever but in case...
        {
            second = Integer.parseInt(num);
        }
        return new Fraction(first, second);
        
    }

}
