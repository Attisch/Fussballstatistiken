package Fussball.Chronologisch;

import Allgemein.Verwendbare;
import Meldung.Wertangabefehler;

/**
 * Enthält die nullbasierten Wochentage, wobei an der 0.Stelle der unbekannte Tag ist und an der 1. der Montag beginnt und an der 7. mit Sonntag endet.
 * @author Attila Kiss
 */
public enum Wochentag {
	UNBEKANNT, MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG, SAMSTAG, SONNTAG;
	
	public String toString() {
		return Verwendbare.zuNomen (super.toString());
	}
	
	public String abkürzung() {
		return toString().substring(0, 2)+ ".";
	}
	
	/**
	 * Gibt die Nummer der Wochentagabkürzung oder einen Fehler zurück
	 * @param abkürzung
	 * @return Nummer das Wochentags
	 * @throws Wertangabefehler
	 */
	public static byte nummer (String abkürzung) throws Wertangabefehler {
		for (Wochentag wt : Wochentag.values())
			if (wt.abkürzung().equals(abkürzung))
				return (byte) wt.ordinal();
		throw new Wertangabefehler ("Die Abkürzung (" +abkürzung +") ist keine gültige Wochentagabkürzung.");
	}
}
