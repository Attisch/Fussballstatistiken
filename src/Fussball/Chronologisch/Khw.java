package Fussball.Chronologisch;

import Allgemein.Verwendbare;
import Meldung.Wertangabefehler;

/**
 * Enthält Daten für eine Kalenderhalbwoche (Khw): Zahl, Monat und Jahr. Eine Khw ist von Dienstag bis Donnerstag oder von Freitag bis Montag sowie gibt es 104 oder maximal 105 Khwn in einem Jahr.
 * @author Attila Kiss
 */
public class Khw implements Comparable<Khw> {
	
	public byte zahl, monatszahl;
	public short jahreszahl;
	
	public final void monat (byte zahl) throws Wertangabefehler {				// Setter
		Verwendbare.wertprüfung (zahl, 1, 12, "Falsche Monatsangabe");
		this.monatszahl = zahl;
	}
	public final void zahl (byte zahl) throws Wertangabefehler {
		Verwendbare.wertprüfung (zahl, 1, 105, "Falsche Kalenderhalbwochenangabe");
		this.zahl = zahl; 
	}
	public final void jahr (short zahl) throws Wertangabefehler {
		zahl = Zeitpunkt.jahreszahlprüfung(zahl);
		Verwendbare.wertprüfung (zahl, 2009, Zeitpunkt.aktuell.khw.jahreszahl+1, "Falsche Jahresangabe");	// UEFA Europa League beginnt in 2009/2010  (Drei punkteregel in 1995/1996 eingeführt)
			this.jahreszahl = zahl;
	}
	
	/**
	 * Zum Kopieren geeignet
	 * @see #zuweisen(byte, byte, short)
	 */
	public Khw() {}
	
	/**
	 * Die fehlende Kalenderhalbwochenzahl wird noch berechnet
	 * @param monat
	 * @param jahr
	 * @throws Wertangabefehler
	 */
	protected Khw (byte monat, short jahr) throws Wertangabefehler {
		monat(monat);
		jahr(jahr);
	}

	public Khw (byte khwZahl, byte monat, short jahr) throws Wertangabefehler {
		geprüfteZuweisung (khwZahl, monat, jahr);
	}
	
	/**
	 * Alle Werte werden geprüft zugewiesen
	 * @param khwZahl
	 * @param monat
	 * @param jahr
	 * @throws Wertangabefehler
	 */
	public void geprüfteZuweisung (byte khwZahl, byte monat, short jahr) throws Wertangabefehler {
		zahl(khwZahl);
		monat(monat);
		jahr(jahr);
	}
	
	/**
	 * Einfache Zuweisung ohne Prüfung
	 * @param khwZahl
	 * @param monat
	 * @param jahr
	 */
	public void zuweisen (byte khwZahl, byte monat, short jahr) {
		zahl = khwZahl;
		monatszahl = monat;
		jahreszahl = jahr;
	}
	
	/**
	 * Die Daten der Kalenderhalbwoche mit den Spielen werden, ohne geprüft zu werden, übernommen
	 * @param khwSpiele ist die {@link KhwSpiele Kalenderhalbwoche mit den Spielen}, deren Daten übernommen werden.
	 * @throws Wertangabefehler
	 */
	public void zuweisen (KhwSpiele khwSpiele) throws Wertangabefehler {
		zuweisen (khwSpiele.khwZahl, khwSpiele.monatszahl, Zeitpunkt.vierstelligesJahr(khwSpiele.jahreszahl));
	}
	
	public void zurücksetzen () {
		jahreszahl = monatszahl = zahl = 0;
	}
	
	/**
	 * Vergleicht zwei Kalenderhalbwochen miteinander, wobei die Rückgabewerte nur 0, 1 oder -1 sein können.
	 * Zu Beachten: Ein Jahr kann zweimal die Angabe 104. oder 105.KHW haben und zwar am Ende des Jahres und am Anfang des folgenden Jahres.
	 * Denn die am Anfang des folgenden Jahres sind die Überbleibsel der letzten KHW des vorherigen Jahres. Deshalb wird die Monatsangabe zur Hilfe herbeigezogen.
	 */
	public int compareTo (Khw andereKhw) {
		return this.zahl==andereKhw.zahl && (this.jahreszahl==andereKhw.jahreszahl || ((this.monatszahl==1 && andereKhw.monatszahl==12 && this.jahreszahl-1==andereKhw.jahreszahl) ||
				(this.monatszahl==12 && andereKhw.monatszahl==1 && this.jahreszahl==andereKhw.jahreszahl-1))) ? 0 : this.jahreszahl< andereKhw.jahreszahl || (this.jahreszahl==andereKhw.jahreszahl &&
				this.monatszahl< andereKhw.monatszahl || (this.monatszahl==andereKhw.monatszahl && (this.zahl< andereKhw.zahl || (this.zahl==104 || this.zahl==105)))) ?  -1 : 1;
	}
	
	public String toString() {
		return zahl +".Khw im " +monatszahl +".Monat " +jahreszahl;
	}
	
	public Khw clone() {
		Khw khw = new Khw();
		this.kopiereNach(khw);
		return khw;
	}
	
	public void kopiereNach (Khw ander) {
		ander.zahl = zahl;
		ander.monatszahl = monatszahl;
		ander.jahreszahl = jahreszahl;
	}

}
