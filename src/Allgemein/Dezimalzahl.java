package Allgemein;

import Meldung.Wertangabefehler;
import Visualität.Ausgabe;

/**
 * Enthält eine positive Dezimalzahl, dessen Werte als Vor- und Nachkommazahlen getrennt aufbewahrt werden. Die Vorkommazahl ist vom Typ 'short' und die Nachkommazahl muss zweistellig sein.
 * Beide Zahlen müssen positive Zahlen sein.
 * @author Attila Kiss
 */
public class Dezimalzahl implements Comparable<Dezimalzahl>{
	
	public short vorKomma;
	public byte nachKomma;

	public Dezimalzahl() {}
	
	public Dezimalzahl (short vorKomma, byte nachKomma) throws Wertangabefehler {
		Verwendbare.wertprüfung (vorKomma, 0, Short.MAX_VALUE, "Die Vorkommazahl ist ungültig.");
		Verwendbare.wertprüfung (nachKomma, 0, 99, "Die Nachkommazahl ist ungültig.");
		this.vorKomma = vorKomma;
		this.nachKomma = nachKomma;
	}

	/**
	 * Zu beachten: Kann eine ungenau Bestimmung der Zahl hinter dem Komma zurückgeben
	 * @return Dezimalzahl vom Typ 'double'
	 */
	public double dezimalzahl() {
		return vorKomma +nachKomma/100.0;
	}
	
	/**
	 * @return 1, 0 oder -1
	 * @param ander
	 */
	public int compareTo (Dezimalzahl ander) {
		return vorKomma==ander.vorKomma && nachKomma==ander.nachKomma ? 0 : vorKomma >ander.vorKomma || (vorKomma==ander.vorKomma && nachKomma >ander.nachKomma) ? 1 : -1;
	}
	
	public Dezimalzahl clone() {
		try {
			return new Dezimalzahl (vorKomma, nachKomma);
		} catch (Wertangabefehler e) {
			Ausgabe.instanz.text (" Das zu clonende Dezimalzahl hat ungültige Werte. Clonen fehlgeschlagen.");
		}
		return null;
	}
	
	public String toString() {
		String nachKommaText = nachKomma +"";
		if (nachKomma< 10)
			nachKommaText = "0" +nachKomma;
		return vorKomma +"." +nachKommaText;
	}
	
	/**
	 * Addiert einen Wert in die zweistellige Zahl nach dem Komma
	 * @param wert ist die Zahl nach dem Komma
	 * @throws Wertangabefehler
	 */
	public void addiere (int wert) throws Wertangabefehler {
		Verwendbare.wertprüfung (wert, 0, 99, "Der Wert zum Addieren ist ungültig.");
		int neuerWert = nachKomma +wert;
		if (neuerWert >99) {
			nachKomma = (byte) (0 +wert -100 +nachKomma);
			vorKomma++;
		}
		else nachKomma = (byte) neuerWert;
		Verwendbare.wertprüfung (vorKomma, 0, Short.MAX_VALUE, "Die Vorkommazahl wurde durch das Addieren ungültig.");
	}
	
	
	/**
	 * Negiert einen Wert in die zweistellige Zahl nach dem Komma
	 * @param wert ist die Zahl nach dem Komma
	 * @throws Wertangabefehler
	 */
	public void negiere (int wert) throws Wertangabefehler {
		Verwendbare.wertprüfung (wert, 0, 99, "Der Wert zum Negieren ist ungültig.");
		int neuerWert = nachKomma -wert;
		if (neuerWert< 0) {
			nachKomma = (byte) (99 -wert +nachKomma +1);
			vorKomma--;
		}
		else nachKomma = (byte) neuerWert;
		Verwendbare.wertprüfung (vorKomma, 0, Short.MAX_VALUE, "Die Vorkommazahl wurde durch das Negieren ungültig.");
	}
	
}
