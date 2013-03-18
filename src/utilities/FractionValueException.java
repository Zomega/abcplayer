package utilities;

/**
 * Custom checkable exception. Indicates that a fraction has been initialized /
 * modified with an impossible value ( i.e. n/0 )
 * 
 * @author woursler and czuo
 * @version RC1
 */
@SuppressWarnings("serial")
public class FractionValueException extends IllegalArgumentException {
	public FractionValueException(String message) {
		super(message);
	}
}
