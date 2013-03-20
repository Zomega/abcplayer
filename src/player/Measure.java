package player;

import java.util.Iterator;
import java.util.List;

import utilities.Fraction;
import utilities.Pair;

public class Measure implements Iterable<Measure> {

	private Measure next;
	private Measure alternateNext = null;

	private List<Pair<Note,Fraction>> notes;
	
	//TODO: Constructor for Measure
	
	//TODO: all these are autogen'd. Assess design carefully.

	public Measure getNext() {
		return next;
	}

	public void setNext(Measure next) {
		this.next = next;
	}

	public Iterator<Measure> iterator() {
		return new MeasureIterator(this);
	}

	public List<Pair<Note, Fraction>> getNotes() {
		return notes;
	}

	public void setNotes(List<Pair<Note, Fraction>> notes) {
		this.notes = notes;
	}

	public Measure getAlternateNext() {
		return alternateNext;
	}

	public void setAlternateNext(Measure alternateNext) {
		this.alternateNext = alternateNext;
	}

}
