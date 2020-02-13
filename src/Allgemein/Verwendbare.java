package Allgemein;

import java.util.ArrayList;

import Meldung.Wertangabefehler;

/**
 * Enthält diverse allgemein verwendbare Methoden und Variablen, die nicht mehr geändert werden
 * @author Attila Kiss
 *
 */
public final class Verwendbare {
	
	private Verwendbare() {}
	
	// Konstanten
	public static final String DOWNLOADS_VON = "/Users/Attisch/Programmierung/Downloads",
								DOWNLOADS_NACH = "Daten";
	public static final char KEINE_GRUPPE = 'Z';
	public static final byte UNBEKANNT = -1,
							 SPALTENANZAHL = 11;
	public static final short MAX_DATEIZEILEN = 424;
	public static final short MIN_JAHR = 2009;
	
	/**
	 * @param zahl, auf deren Betrag der Fokus liegt
	 * @return positive Zahl
	 */
	public static int betrag (int zahl) {
		if (zahl< 0)
			return zahl*(-1);
		return zahl;
	}
	
	/**
	 * @param liste mit Bytes
	 * @return eine Liste mit bytes, die aus der angegebenen Liste kopiert wurde.
	 */
	public static byte[] byteListe (ArrayList<Byte> liste) {
		byte[] neueListe = new byte[liste.size()];
		
		for (byte b=0; b<neueListe.length; b++)
			neueListe[b] = liste.get(b);
		return neueListe;
	}
		
	/**
	 * @return Eine Leerzeichenfolge, deren Anzahl das Ergebnis des Maximums minus der Textlänge ist.
	 * @param text von dessen Länge die Anzahl der Leerzeichen anghängen
	 * @param max ist das Maximum der Anzahl der erreichbaren Leerzeichen
	 */
	public static String leerzeichen (String text, int max) {
		String leerzeichen = "";
		
		for (int i = 0; i< max-text.length(); i++)
			leerzeichen += " ";
		
		return leerzeichen;
	}
		
	/**
	 * Ergänzt die angegebene Zahl, falls sie einstellig ist, mit einer Null vorne dran und gibt sie in Textformat zurück.
	 * @param zahl
	 * @return Zahl als Text
	 */
 	public static String nullformatText (byte zahl) {
		if (zahl<10)
			return "0"+zahl;
		else return ""+zahl;
	}
	
	/**
	 * @return gerundete Prozentangabe von 0-100, ansonsten Fehlermeldung
	 * @param zähler
	 * @param nenner
	 * @throws Wertangabefehler wenn Nenner gleich Null, Prozentangabe unter Null oder über 100
	 */
	public static byte prozent (int zähler, int nenner) throws Wertangabefehler {
		if (nenner==0)
			throw new Wertangabefehler ("Fehler bei der Prozentrechnung: Nenner darf nicht 0 sein.");
		byte wert = (byte) (Math.round(zähler*100.0/nenner));
		wertprüfung (wert, 0, 100, "Fehler bei der Prozentangabe. Prüfe Zähler: " +zähler +", prüfe Nenner: " +nenner);
		return wert;
	}
	
	/**
	 * Prüft die angegebene Zahl und ergänzt die Ausgabe mit Leerzeichen vorne dran, wenn die Dezimalstellen der Zahl kleiner sind als die maximalen Dezimalstellen.
	 * Falls die Zahl einen negativen Wert beinhaltet, muss das Vorzeichen als eine zusätzliche Dezimalstelle mit bei der Angabe dazu gerechnet werden.
	 * BSP: Zahl=99 -> 2 Dezimlastellen. Zahl=-99 -> 3 Dezimalstellen.
	 * @param zahl
	 * @param maxDezimalstellen
	 * @param vorne ist true, wenn die Leerzeichen vorne angehangen werden sollen, ansonsten werden sie hinten angehangen
	 * @return Die Zahl als Text
	 */
	public static String zahlformat (int zahl, int maxDezimalstellen, boolean vorne) {
		String text = "";
		long max = 1;
		boolean negativeZahl = false;
		
		// Vorzeichen klären
		if (zahl < 0) {
			negativeZahl = true;
			zahl *= -1;
			maxDezimalstellen--;
		}
		
		// Leerzeichen festsetzen
		if (zahl>=0 && maxDezimalstellen >1) {
			while (--maxDezimalstellen > 0)
				max *= 10;
			do {
				if (zahl< max)
					text += " ";
				max /= 10;
			} while (max >zahl && max!=1);
		}
		
		// Vorzeichen und Zahl einfügen
		if (negativeZahl)
			if (vorne)
				text += "-";
		if (vorne)
			text += zahl;
		else {
			if (negativeZahl)
				text = "-" +zahl +text;
			else text = zahl +text;
		}
		return text;
	}
	
	/**
	 * Fehlermeldung wird erzeugt, falls die Prüfung des Wertes nicht bestanden wurde. Dabei wird eine konstante Fehlermeldung, die die Parameter enthält, mit einer Fehlermeldung ergänzt.
	 * @param wert der geprüft werden soll
	 * @param min ist der kleinstmögliche Wert
	 * @param max ist der größtmögliche Wert
	 * @param fehlermeldung falls der Wert außerhalb der Grenzwerten liegt
	 * @throws Wertangabefehler
	 */
	public static void wertprüfung (int wert, int min, int max, String fehlermeldung) throws Wertangabefehler {
		if (wert< min || wert >max)
			throw new Wertangabefehler (wert+", der außerhalb von "+min+"-"+max+" liegt. " +fehlermeldung);
	}
	
	/**
	 * @return ein Nomen
	 * @param grossbuchstaben im Wort (Konstante eines Enums) und zwar ausschließlich
	 */
	public static String zuNomen (String grossbuchstaben) {
		return grossbuchstaben.substring(0, 1) + grossbuchstaben.substring(1).toLowerCase();
	}
	
	/**
	 * @return die grösste positive Zahl aus der Liste der Zahlen
	 * @param zahlen
	 */
	public static byte grössteZahl (byte...zahlen) {
		byte grösste = 0;
		for (byte b : zahlen)
			if (b >grösste)
				grösste = b;
		return grösste;
	}

	

}
