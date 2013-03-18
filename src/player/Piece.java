package player;

import java.util.List;

public class Piece {

	/**
	 * The List of all the starting measures for each voice.
	 */
	private List<Measure> voices;

	/**
	 * @return a list of all Voices parameterized by their first measure.
	 */
	public List<Measure> getVoices() {
		return this.voices;
	}

}
