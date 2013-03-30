package player;

import java.util.List;
import java.util.Map;

import sound.Pitch;

public class Key {
	private Map<Character, Character> accidentals;

	// TODO: Add method to add overrides.

	public Key(List<String> accidentals) {
		for (String accidental : accidentals) {
			// TODO: Fill in documentation.
			// TODO: ensure case casting.
			// TODO: Better mapping? Flat -> -1, etc?
			this.accidentals.put(accidental.charAt(0), accidental.charAt(1));
		}
	}

	public Pitch getPitch(String note) {
		// TODO: Fill in.
		return null;
	}
}
