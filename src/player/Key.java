package player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sound.Pitch;

public class Key {
	private Map<Character, Character> accidentals;

	// TODO: Add method to add overrides.
	// TODO: Add exceptions and safeties.

	public Key() {
		this.accidentals = new HashMap<Character, Character>();
	}

	public Key(List<String> accidentals) {
		this();
		for (String accidental : accidentals) {
			// TODO: Fill in documentation.
			// TODO: ensure case casting.
			// TODO: Better mapping? Flat -> -1, etc?
			this.accidentals.put(accidental.charAt(0), accidental.charAt(1));
		}
	}

	/**
	 * Parse a abc note into it's Pitch object.
	 * 
	 * @param note
	 *            A char to transform in A-G...
	 * @return the Pitch requested, in this key.
	 */
	public Pitch getPitch(char note) {
		int accidental = 0;
		if (this.accidentals.containsKey(note)) {
			switch (accidentals.get(note)) {
			case '#':
				accidental = 1;
				break;
			case 'b':
				accidental = -1;
				break;
			}
		}
		return new Pitch(note, accidental);
	}

	/**
	 * Returns true if the Keys produce the same output on A-G (middle octave).
	 */
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other.getClass() != this.getClass())
			return false;
		Key otherKey = (Key) other;
		System.out.println(otherKey.getPitch('A'));
		for (char note : new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' }) {
			if (this.getPitch(note).equals(otherKey.getPitch(note)))
				continue;
			return false;
		}
		return true;
	}
}
