package player;

import java.util.Iterator;

public class Voice implements Iterable<Measure> {
	public final String name;
	private Measure firstMeasure;

	public Voice(String name, Measure firstMeasure) {
		this.name = name;
		this.firstMeasure = firstMeasure;
	}

	/**
	 * @return the first Measure of this voice.
	 */
	public Measure getStart() {
		return firstMeasure;
	}

	/**
	 * @return the last Measure of this voice so far.
	 */
	public Measure getEnd() {
		Iterator<Measure> i = this.iterator();
		Measure last = null;
		while (i.hasNext()) {
			last = i.next();
		}
		return last;
	}

	public Iterator<Measure> iterator() {
		return this.getStart().iterator();
	}

}
