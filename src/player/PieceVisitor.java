package player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SequencePlayer;
import utilities.Fraction;
import utilities.Pair;

/**
 * Converts Piece instances to SequencePlayer instances. This class has only
 * static methods. It should not be instantiated.
 * 
 * @author woursler
 * @version beta
 */
public class PieceVisitor {
	/**
	 * @param piece
	 *            A Piece which we wish to process
	 * @return A SequencePlayer with the data from piece loaded in.
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 */
	public static SequencePlayer process(Piece piece)
			throws MidiUnavailableException, InvalidMidiDataException {

		/**
		 * The fractional global time at which the current measure started.
		 */
		Fraction globalTime = new Fraction(0);
		int ticksPerQuarterNote = fractionToTicks(new Fraction(1, 4),
				piece.getSmallestDivision());
		SequencePlayer player = new SequencePlayer(piece.getTempo(),
				ticksPerQuarterNote);
		Fraction smallestDivision = piece.getSmallestDivision();
		for (Voice voice : piece.getVoices()) {
			for (Measure measure : voice) {
				for (Pair<Note, Fraction> p : measure.getNotes()) {
					// Unpack the pair.
					Note note = p.first;
					Fraction offset = p.second;

					player.addNote(
							note.pitch.toMidiNote(),
							PieceVisitor.fractionToTicks(
									globalTime.plus(offset),
									smallestDivision),
							PieceVisitor.fractionToTicks(note.duration,
									smallestDivision));
				}

				// Move forward one measure.
				globalTime = globalTime.plus(piece.getMeter());
			}
		}
		return player;
	}

	/**
	 * Convert a time ( in fractional length ) to ticks.
	 * 
	 * @param time
	 *            The time to convert.
	 * @param divisionLength
	 *            the length of a single tick.
	 * @return the number of ticks in time.
	 */
	private static int fractionToTicks(Fraction time, Fraction divisionLength) {
		// The int case should be exact, since time should be an integer
		// multiple of the division length.
		System.out.println( time );
		System.out.println( divisionLength );
		System.out.println( time.quotient(divisionLength).approximation() );
		System.out.println( (int) time.quotient(divisionLength).approximation() );
		
		
		return (int) time.quotient(divisionLength).approximation();
	}
}
