package player;

import sound.Pitch;
import utilities.Fraction;

/**
 * Represents a single note. A not has pitch and duration.
 * 
 * The Note class is immutable (public final model).
 * 
 * @author woursler
 * @version RC1
 */
public class Note {
	
	/**
	 * The length of time the note lasts. Note that this is relative absolute time, not a default not length.
	 */
	public final Fraction duration;
	
	/**
	 * The pitch of the note.
	 */
	public final Pitch pitch;

	/**
	 * Simple constructor. Directly initializes both elements.
	 * @param duration
	 * @param pitch
	 */
    public Note(Fraction duration, Pitch pitch) {
        this.duration = duration;
        this.pitch = pitch;
    }
    
	public Fraction getDuration() {
		return duration;
	}
	
	public boolean equals(Object other) {
	    if (other instanceof Note) {
            Note o = (Note) other;
            return ((this.duration.equals(o.duration)) && (this.pitch.equals(o.pitch)));
        }
        return false;
	}
	
	public String toString(){
	    return this.duration.toString()+" "+this.pitch.toString();
	}

}
