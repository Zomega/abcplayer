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
		Fraction measureTime = new Fraction(0);
		int conversionFactor = 0; // TODO: The number of ticks per unit
									// fraction.

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
							.fractionToTicks(measureTime.plus(offset),
									conversionFactor), PieceVisitor
							.fractionToTicks(note.getDuration(),
									conversionFactor));
				}
				measureTime = measureTime.plus(1);
			}
		}
		return null;
	}

	private static int fractionToTicks(Fraction f, int conversionFactor) {
		return 0; // TODO
	}
}
