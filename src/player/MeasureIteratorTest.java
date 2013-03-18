package player;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class MeasureIteratorTest {

	@Test
	public void basicRepeat() {
		// m1 |: m2 | m3 :| m4 |]

		Measure m1 = new Measure();
		Measure m2 = new Measure();
		Measure m3 = new Measure();
		Measure m4 = new Measure();
		m1.setNext(m2);
		m2.setNext(m3);
		m3.setNext(m2);
		m3.setAlternateNext(m4);

		Iterator<Measure> i = m1.iterator();
		assertEquals(i.next(), m1);
		assertEquals(i.next(), m2);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m2);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m4);
		assertEquals(i.next(), null);
	}

	@Test
	public void nestedRepeat() {
		// m1 |: m2 |: m3 | m4 :| m5 :| m6 |]

		Measure m1 = new Measure();
		Measure m2 = new Measure();
		Measure m3 = new Measure();
		Measure m4 = new Measure();
		Measure m5 = new Measure();
		Measure m6 = new Measure();
		m1.setNext(m2);
		m2.setNext(m3);
		m3.setNext(m4);
		m4.setNext(m3);
		m4.setAlternateNext(m5);
		m5.setNext(m2);
		m5.setAlternateNext(m6);

		Iterator<Measure> i = m1.iterator();
		assertEquals(i.next(), m1);
		assertEquals(i.next(), m2);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m4);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m4);
		assertEquals(i.next(), m5);
		assertEquals(i.next(), m2);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m4);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m4);
		assertEquals(i.next(), m5);
		assertEquals(i.next(), m6);
		assertEquals(i.next(), null);
	}

	@Test
	public void twoEndingRepeat() {
		// m1 |: m2 |[1 m3 |[2 m4 :| m5 |]

		Measure m1 = new Measure();
		Measure m2 = new Measure();
		Measure m3 = new Measure();
		Measure m4 = new Measure();
		Measure m5 = new Measure();
		m1.setNext(m2);
		m2.setNext(m3);
		m2.setAlternateNext(m4);
		m3.setNext(m2);
		m4.setNext(m5);

		Iterator<Measure> i = m1.iterator();
		assertEquals(i.next(), m1);
		assertEquals(i.next(), m2);
		assertEquals(i.next(), m3);
		assertEquals(i.next(), m2);
		assertEquals(i.next(), m4);
		assertEquals(i.next(), m5);
		assertEquals(i.next(), null);

	}
}