package utilities;

/**
 * Custom checkable exception.
 * Indicates that a fraction has been initialized / modified
 * with an impossible value ( i.e. n/0 )
 * @author catherine
 *
 */
public class FractionValueException extends IllegalArgumentException {
    public FractionValueException(String message) {
        super(message);
    }
}
