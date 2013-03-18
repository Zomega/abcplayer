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
 */
public class PieceVisitor {
	public static SequencePlayer process(Piece piece)
			throws MidiUnavailableException, InvalidMidiDataException {

		// ///////////////TODO::::
		int beatsPerMinute = 0; // TODO
		int ticksPerQuarterNote = 0;// TODO

		/**
		 * The fractional global time at which the current measure started.
		 */
		Fraction globalTime = new Fraction(0);

		// //END TODO

		SequencePlayer player = new SequencePlayer(beatsPerMinute,
				ticksPerQuarterNote);
		for (Measure voice : piece.getVoices()) {
			for (Measure measure : voice) {
				for (Pair<Note, Fraction> p : measure.getNotes()) {
					// Unpack the pair.
					Note note = p.first;
					Fraction offset = p.second;

					player.addNote(note.getPitch().toMidiNote(), PieceVisitor
							.fractionToTicks(globalTime.plus(offset),
									piece.getSmallestDivision()), PieceVisitor
							.fractionToTicks(note.getDuration(),
									piece.getSmallestDivision()));
				}

				// Move forward one measure.
				globalTime = globalTime.plus(piece.getMeter());
			}
		}
		return null;
	}
	
	/**
	 * Convert a time ( in fractional length ) to ticks.
	 * @param time The time to convert.
	 * @param divisionLength the length of a single tick.
	 * @return the number of ticks in time.
	 */
	private static int fractionToTicks(Fraction time, Fraction divisionLength) {
		return 0; // TODO
	}
}
