package Fussball.Chronologisch;

import Meldung.Wertangabefehler;

/**
 * Enthält einen Tag mit Kalender- und Wochentagangabe
 * Zu beachten: Der Kalendertag muss extra geprüft werden: {@link #kalendertagprüfung(byte, short)}
 * @param <T> Typ des Objekts, das den Tag mit Daten erweitert (Für den Vergleichsoperator "Comparable" zu bestimmen)
 * @author Attila Kiss
 */
abstract class Tag<T extends Tag<T>> implements Comparable<T>{

	byte kalendertag;
	byte wochentag;
	
	// Getter
	public final byte wochentag() { return wochentag; }
	public final byte kalendertag() { return kalendertag; }
	
	// Setter
	public final void wochentag (byte nr) throws Wertangabefehler {
		Allgemein.Verwendbare.wertprüfung (nr, 0, 7, "Falsche Wochentagangabe");
		this.wochentag = nr;
	}
	
	protected Tag() {}
	
	protected Tag (byte kalendertag, byte wochentag) throws Wertangabefehler {
		wochentag(wochentag);
		this.kalendertag = kalendertag;
	}
	
	public String toString() {
		return Wochentag.values()[wochentag].abkürzung();
	}
	
	/**
	 * Prüft den Kalendertag in Bezug auf Monat und Jahr
	 * @param monat
	 * @param jahr
	 * @throws Wertangabefehler
	 */
	public void kalendertagprüfung (byte monat, short jahr) throws Wertangabefehler {
		Allgemein.Verwendbare.wertprüfung(kalendertag, 1, Zeitpunkt.maxMonatstage(monat, jahr), "Falsche Kalendertagangabe");
	}
	
	public int compareTo (T ander) {
		return kalendertag==ander.kalendertag && wochentag==ander.wochentag ? 0 : kalendertag< ander.kalendertag || (kalendertag==ander.kalendertag && wochentag< ander.wochentag) ? -1 : 1;
	}

	public void unbekannterWochentag() {
		wochentag = (byte) Wochentag.UNBEKANNT.ordinal();
	}
}
