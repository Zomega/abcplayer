package player;

import java.util.Iterator;

public class MeasureIterator implements Iterator<Measure> {
	/**
	 * The measure we are currently initialized with.
	 */
	Measure start;

	/**
	 * The measure we are currently concerned with.
	 */
	Measure current;

	/**
	 * Constructor
	 * 
	 * @param start
	 *            The first Measure of the music we will iterate through.
	 */
	public MeasureIterator(Measure start) {
		this.start = start;
		this.current = null;
	}

	public boolean hasNext() {

		// Somewhat amusingly, this only occurse when we have yet to begin
		// iterating, which implies we do have a next element.
		if (this.current == null)
			return true;
		
		// This will happen at the end of the piece. We will never have that we
		// do not have a next, but we do have an alternative.
		if (this.current.getNext() == null)
			return false;
		
		// We do have an explicit next element, so return true.
		return true;
	}

	public Measure next() {
		// TODO: This iterator doesn't handle repeats at all, let alone nested repeats.
		// If we're just starting out...
		if( this.current == null ) {
			this.current = this.start;
			return this.current;
		}
		
		// If we're at the end (this shouldn't get called, really).
		if( !this.hasNext() )
			return null;
		
		this.current = this.current.getNext();
		return this.current;	
		
	}

	/**
	 * THIS METHOD DOES NOTHING EXCEPT THROW AN ERROR.<br/>It's not supposed to do
	 * anything else, because you cannot remove Measure objects while iterating
	 * over them.
	 * 
	 * @throws e
	 *          all the time, every time.
	 */
	public void remove() throws RuntimeException {
		//TODO: special exception type?
		throw new RuntimeException("Removing Measures is not supported.");
	}

}
