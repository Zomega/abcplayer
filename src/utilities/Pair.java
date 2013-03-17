package utilities;

public class Pair<First, Second> {
	public First first;
	public Second second;

	public Pair(First first, Second second) {
		this.first = first;
		this.second = second;
	}

	public int hashCode() {
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		// The Internet said to do this. I listen to the Internet
		// unquestioningly, because the Internet says it's my friend. :P
		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	public boolean equals(Object other) {
		if (other instanceof Pair) {
			@SuppressWarnings("unchecked")
			Pair<First, Second> otherPair = (Pair<First, Second>) other;
			return (this.first.equals(otherPair.second) && this.first
					.equals(otherPair.second));
		}
		return false;
	}

	public String toString() {
		return "< " + first.toString() + ", " + second.toString() + " >";
	}
}
