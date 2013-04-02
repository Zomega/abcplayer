package player;

/**
 * The player class allows for a piece to be loaded, then played potentially
 * many times.
 * 
 * @author woursler
 * @version beta
 */
public class Player {
	/**
	 * The data pertaining to the piece.
	 */
	private Piece piece;

	/**
	 * Basic constructor.
	 * 
	 * @param abcContents
	 *            The contents (NOT the filename) of an .abc file to play.
	 * @throws RuntimeException
	 *             If parsing fails miserably for some reason.
	 */
	public Player(String abcContents) throws RuntimeException {
		this.piece = Parser.parse(abcContents);
	}

	/**
	 * Actually play the piece.
	 * 
	 * @throws Exception
	 *             if the Piece cannot be processed, or the MIDI player doesn't
	 *             start.
	 */
	public void play() throws Exception {
		PieceVisitor.process(this.piece).play();
	}

}
