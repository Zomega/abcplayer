package player;

import java.util.ArrayList;
import java.util.List;


import utilities.Fraction;

/**
 * Data structure that represents a Piece of music.
 * 
 * @author woursler
 * @version alpha
 */
public class Piece {

	/**
	 * The title of the piece.
	 */
	private String title;

	/**
	 * The composer of the piece.
	 */
	private String composer;

	/**
	 * Track Number of the piece.
	 */
	private int trackNumber;

	/**
	 * Default length or duration of a note.
	 */
	private Fraction defaultNoteLength;

	/**
	 * It determines the sum of the durations of all notes within a measure
	 */
	private Fraction meter;

	/**
	 * The number of default-length notes per minute.
	 */
	private int tempo;

	/**
	 * Determines the key signature for the piece.
	 */
	private String key;

	/**
	 * The List of all the starting measures for each voice.
	 */
	private List<Voice> voices;

	/**
	 * The (largest, ideally) smallest division needed such that the length of
	 * each note (and rest) is an integer multiple.
	 */
	private Fraction smallestDivision;

    /**
     * Constructor
     */
	public Piece() {
	    this.composer = "Unknown";
	    this.voices = new ArrayList<Voice>();
	}
	
	// TODO: Careful inventory of methods. All autogenerated atm.
	public Fraction getMeter() {
		return meter;
	}

	public void setMeter(Fraction meter) {
		this.meter = meter;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public Fraction getSmallestDivision() {
		return smallestDivision;
	}

	public void setSmallestDivision(Fraction smallestDivision) {
		this.smallestDivision = smallestDivision;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public Fraction getDefaultNoteLength() {
		return defaultNoteLength;
	}

	public void setDefaultNoteLength(Fraction defaultNoteLength) {
		this.defaultNoteLength = defaultNoteLength;
	}

	public List<Voice> getVoices() {
		return voices;
	}

	public void setVoices(List<Voice> voices) {
		this.voices = voices;
	}
	
	public void addVoice(Voice voice) {
	    this.voices.add(voice);
	}

	public Voice getVoice(String name) {
		for( Voice voice : this.voices ) {
			if( voice.name.equals( name ) ) {
				return voice;
			}
		}
		return null;
	}

}
