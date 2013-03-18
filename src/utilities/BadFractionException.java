package utilities;

/**
 * Custom checkable exception.
 * Indicates that a fraction has been initialized / modified
 * to have one of the following 'bad' values which are musically incorrect:
 * denominator of 0, or
 * negative numerator or denominator
 * @author catherine
 *
 */
public class BadFractionException extends Exception {
    public BadFractionException(String message) {
        super(message);
    }
}
