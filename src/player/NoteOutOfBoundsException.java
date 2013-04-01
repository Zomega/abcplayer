package player;

/**
 * Thrown when an attempt is made to insert a note into a measure it does not
 * fit within.
 * 
 * @author woursler
 * @version RC1
 */
@SuppressWarnings("serial")
public class NoteOutOfBoundsException extends RuntimeException {
	/**
	 * Basic constructor.
	 * 
	 * @param message
	 *            a Human Readable error message.
	 */
	public NoteOutOfBoundsException(String message) {
		super(message);
	}
}
