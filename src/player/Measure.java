package player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utilities.Fraction;
import utilities.Pair;

/**
 * @author woursler
 * @version beta
 */
public class Measure implements Iterable<Measure> {

	/**
	 * Length of the measure.
	 */
	private final Fraction length;
	
	/**
	 * A list of notes, each associated with their start times.
	 */
	private List<Pair<Note, Fraction>> notes;
	
	// Linking Structure...
	
	/**
	 * The typical next measure in the larger piece.
	 */
	private Measure next;

	/**
	 * An alternate next measure, e.g. the escape from a repeat.
	 */
	private Measure alternateNext = null;


	/**
	 * Full constructor. All values are explicit. If you want to assign an
	 * existing list of notes, use this one.
	 * @throws NoteOutOfBoundsException if any of the listed notes is invalid.
	 */
	public Measure(Measure next, Measure alternateNext,
			List<Pair<Note, Fraction>> notes, Fraction length) throws NoteOutOfBoundsException {
		this.next = next;
		this.alternateNext = alternateNext;
		this.notes = new ArrayList<Pair<Note, Fraction>>();
		// Add each new note in a safe manner.
		for (Pair<Note, Fraction> note : notes)
			this.addNote(note.first, note.second);
		this.length = length;

	}

	/**
	 * Structure Measure. Constructs an empty list of notes automatically.
	 */
	public Measure(Measure next, Measure alternateNext, Fraction length) {
		this.next = next;
		this.alternateNext = alternateNext;
		this.notes = new ArrayList<Pair<Note, Fraction>>();
		this.length = length;
	}

	/**
	 * Constructs a Measure based only off the next Measure.
	 */
	public Measure(Measure next, Fraction length) {
		this(next, null, length);
	}

	/**
	 * Default Constructor. Initializes all values to default.
	 */
	public Measure( Fraction length ) {
		this(null, length);
	}

	/**
	 * Returns an iterator which will start with this measure, and continue
	 * until the end of the piece is reached.
	 */
	public Iterator<Measure> iterator() {
		return new MeasureIterator(this);
	}

	/**
	 * Getter for this.next
	 * 
	 * @return the typical next Measure.
	 */
	public Measure getNext() {
		return next;
	}

	/**
	 * Setter for this.next
	 */
	public void setNext(Measure next) {
		this.next = next;
	}

	/**
	 * Getter for this.alternateNext
	 * 
	 * @return the alternate next Measure.
	 */
	public Measure getAlternateNext() {
		return alternateNext;
	}

	/**
	 * Setter for this.alternateNext
	 */
	public void setAlternateNext(Measure alternateNext) {
		this.alternateNext = alternateNext;
	}

	/**
	 * Getter for this.notes
	 * 
	 * @return a List of the Note / Time pairs. This list is a shallow copy of
	 *         this.notes, but both Note and Fraction are immutable, so it
	 *         should pose no risk.
	 */
	public List<Pair<Note, Fraction>> getNotes() {
		return new ArrayList<Pair<Note, Fraction>>(notes);
	}

	/**
	 * Safe method to add a note to this measure.
	 * 
	 * @param note
	 *            The Note to add...
	 * @param startTime
	 *            The time at which the note starts with respect to the start of
	 *            the measure.
	 * @throws NoteOutOfBoundsException if the passed note is not fully contained in the measure.
	 */
	public void addNote(Note note, Fraction startTime ) throws NoteOutOfBoundsException {
		
		// Check to ensure the note is fully within the measure...
		// Check to ensure it starts after 0...
		if( startTime.numerator < 0 )
			throw new NoteOutOfBoundsException("Tried to add a note that started at " + startTime +"." );
		if( note.duration.numerator <= 0 )
			throw new NoteOutOfBoundsException("Tried to add a non-positive duration note.");
		Fraction endTime = startTime.plus( note.duration );
		if( endTime.minus( this.length ).numerator > 0 )
			throw new NoteOutOfBoundsException("Tried to add a note which ended after the measure.");
		
		// Rests have null pitch. We don't actually want to add them to our notes.
		if( note.pitch == null ) return;
		
		this.notes.add(new Pair<Note, Fraction>(note, startTime ));
	}

}
