package player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utilities.Fraction;
import utilities.Pair;

/**
 * @author woursler
 * @version alpha
 */
public class Measure implements Iterable<Measure> {

	/**
	 * The typical next measure in the larger piece.
	 */
	private Measure next;

	/**
	 * An alternate next measure, e.g. the escape from a repeat.
	 */
	private Measure alternateNext = null;

	/**
	 * A list of notes, each associated with their start times.
	 */
	private List<Pair<Note, Fraction>> notes;

	/**
	 * Full constructor. All values are explicit. If you want to assign an
	 * existing list of notes, use this one.
	 */
	public Measure(Measure next, Measure alternateNext,
			List<Pair<Note, Fraction>> notes) {
		this.next = next;
		this.alternateNext = alternateNext;
		this.notes = new ArrayList<Pair<Note, Fraction>>();
		for (Pair<Note, Fraction> note : notes)
			this.addNote(note.first, note.second);
	}

	/**
	 * Structure Measure. Constructs an empty list of notes automatically.
	 */
	public Measure(Measure next, Measure alternateNext) {
		this(next, alternateNext, new ArrayList<Pair<Note, Fraction>>());
	}

	/**
	 * Constructs a Measure based only off the next Measure.
	 */
	public Measure(Measure next) {
		this(next, null);
	}

	/**
	 * Default Constructor. Initializes all values to default.
	 */
	public Measure() {
		this(null);
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
	 * @return a List of the Note / Time pairs. Please do not modify this list.
	 *         Java's stupidity with regards to generics prevents me from
	 *         effectively copying the list, so mutations will actually mutate a
	 *         private member, which rather defeats the point.
	 * 
	 *         TODO:Fix
	 */
	public List<Pair<Note, Fraction>> getNotes() {
		return notes;
	}

	/**
	 * Safe method to add a note to this measure.
	 * 
	 * @param note
	 *            The Note to add...
	 * @param offset
	 *            The time at which the note starts with respect to the start of
	 *            the measure.
	 */
	public void addNote(Note note, Fraction offset) {
		// TODO: check that the note is within the measure?
		// This requires a notion of measure length.
		this.notes.add(new Pair<Note, Fraction>(note, offset));
	}

}
