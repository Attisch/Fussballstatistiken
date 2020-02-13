package Visualität;

import java.io.IOException;

import Prog1Tools.IOTools;
import Allgemein.Verwendbare;
import Meldung.Tastatureingabefehler;

/**
 * Enthält eine Variable die mit einer Instanz der Ausgabe geladen wird
 * @author Attila Kiss
 */
public final class Ausgabe {

	private Ausgabe() {}

	/**
	 * Der für die Ausgabe festgelegte Instanz
	 */
	public static final Ausgabefähig instanz = Console.ladeConsole();

	/**
	 * Gibt das Menü mit den angegebenen Menüpunkten aus und gibt die Wahl der Eingabe zurück
	 * @param titel des Menüs
	 * @param ende des Menüs
	 * @param punkte des Menüs
	 * @return die gewählte Nummer des Menüpunktes
	 */
	public static byte menü (String titel, String ende, String... punkte) {
		byte nr = 1;
		
		menütitel (titel);
		for (String punkt : punkte)
			Ausgabe.instanz.text (Verwendbare.zahlformat (nr++, 2, true) +": " +punkt);
		Ausgabe.instanz.text ("\n 0: " +ende);
		do {
			nr = IOTools.readByte ("\nGib die Zahl deiner Wahl ein: ");
		} while (nr< 0 || nr >punkte.length);
		
		return nr;
	}

	/**
	 * Gibt im Menükontext den Titel aus
	 * @param text des Titels
	 */
	static void menütitel (String text) {	
		Ausgabe.instanz.text ("\n"+text);
		for (int i = 0; i< text.length(); i++)
			Ausgabe.instanz.textteil ("=");
		Ausgabe.instanz.text ("\n");
	}
	
	public static void weiterMitEnter() throws Tastatureingabefehler {
		Ausgabe.instanz.text ("Weiter mit Enter-Taste");
		try {
		    System.in.read();
		}  
		catch (IOException e) {
			throw new Tastatureingabefehler ("Tastatureingabefehler: "+e.getMessage());
		} 
	}
}
