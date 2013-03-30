package player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CircleOfFifths
 * 
 * @author kimtoy
 * @category no_didit
 * 
 */
public class CircleOfFifthsTest {
	@Test
	public void testMajorKeySignatures() {
		assertEquals(CircleOfFifths.getKeySignature("C"), new Key(
				new ArrayList<String>()));
		assertEquals(CircleOfFifths.getKeySignature("G"),
				new Key(Arrays.asList(new String[] { "F#" })));
		assertEquals(CircleOfFifths.getKeySignature("E"),
				new Key(Arrays.asList(new String[] { "F#", "C#", "G#", "D#" })));
		assertEquals(CircleOfFifths.getKeySignature("Eb"),
				new Key(Arrays.asList(new String[] { "Bb", "Eb", "Ab" })));
		assertEquals(
				CircleOfFifths.getKeySignature("Cb"),
				new Key(Arrays.asList(new String[] { "Bb", "Eb", "Ab", "Db",
						"Gb", "Cb", "Fb" })));
	}

	@Test
	public void testMinorKeySignatures() {
		assertEquals(CircleOfFifths.getKeySignature("am"), new Key(
				new ArrayList<String>()));
		assertEquals(CircleOfFifths.getKeySignature("em"),
				new Key(Arrays.asList(new String[] { "F#" })));
		assertEquals(CircleOfFifths.getKeySignature("c#m"),
				new Key(Arrays.asList(new String[] { "F#", "C#", "G#", "D#" })));
		assertEquals(CircleOfFifths.getKeySignature("cm"),
				new Key(Arrays.asList(new String[] { "Bb", "Eb", "Ab" })));
		assertEquals(
				CircleOfFifths.getKeySignature("abm"),
				new Key(Arrays.asList(new String[] { "Bb", "Eb", "Ab", "Db",
						"Gb", "Cb", "Fb" })));
	}

	@Test
	public void testIgnoreCase() {
		assertEquals(CircleOfFifths.getKeySignature("c"), new Key(
				new ArrayList<String>()));
	}
}
