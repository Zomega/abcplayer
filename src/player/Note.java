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
	public final Fraction duration;
	public final Pitch pitch;

    public Note(Fraction duration, Pitch pitch) {
        this.duration = duration;
        this.pitch = pitch;
    }
	


	public Fraction getDuration() {
		return duration;
	}

	//TODO: Why is this here????
	public void setDuration(Fraction duration) {

	}



}
