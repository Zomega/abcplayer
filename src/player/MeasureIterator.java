package player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
	 * 
	 */
	Map< Measure, Integer > timesSeen;

	/**
	 * Constructor
	 * 
	 * @param start
	 *            The first Measure of the music we will iterate through.
	 */
	public MeasureIterator(Measure start) {
		this.start = start;
		this.current = null;
		this.timesSeen = new HashMap< Measure, Integer >();
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
		
		//TODO: Detect infinite / invalid linking structures.
		
		// If we're just starting out...
		if( this.current == null ) {
			this.current = this.start;
			return this.current;
		}
		
		// If we're at the end(this shouldn't get called, really).
		if( !this.hasNext() )
			return null;
		
		// Increment the number of times that we've seen this measure.
		int timesSeenBefore = 1;
		if( this.timesSeen.containsKey(this.current) )
			timesSeenBefore += this.timesSeen.get(this.current);
		this.timesSeen.put(this.current, timesSeenBefore);
		
		//Now we need to check which branch we should take...
		
		//If the measure isn't a branching one we always take the first branch.
		if( this.current.getAlternateNext() == null )
			this.current = this.current.getNext();
		// If we've seen this particular measure an odd number of times, we should take it's first branch (e.g. the first time we see it.)
		else if( timesSeenBefore % 2 == 1 )
			this.current = this.current.getNext();
		// If we've seen it an even number of times, take the alternate branch.
		else
			this.current = this.current.getAlternateNext();
		
		return this.current;
	}

	/**
	 * THIS METHOD DOES NOTHING EXCEPT THROW AN ERROR.<br/>It's not supposed to do
	 * anything else, because you cannot remove Measure objects while iterating
	 * over them, or ever for that matter.
	 * 
	 * @throws e
	 *          all the time, every time.
	 */
	public void remove() throws RuntimeException {
		//TODO: special exception type?
		throw new RuntimeException("Removing Measures is not supported.");
	}

}
