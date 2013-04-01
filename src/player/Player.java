package player;

public class Player {
	private Piece piece;
	
	public Player( String abcContents ) throws RuntimeException {
		this.piece = Parser.parse(abcContents);
	}
	
	public void play() throws Exception {
		PieceVisitor.process(this.piece).play();
	}

}
