package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein festgelegtes Spiel, dessen Ergebnis bestimmt wurde.
 * @author Attila Kiss
 */
public class FestgelegtesErgebnis extends SpielMitErgebnis implements FesterSieger, FestgelegterSieger {
			
	/**
	 * Erzeugt ein festgelegtes Spiel, dessen Ergebnis bestimmt wurde.
	 * @author Attila Kiss
	 */
	public FestgelegtesErgebnis (byte[] terminteile, String heimteam, String auswärtsteam, byte heimtore, byte auswärtstore, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, heimtore, auswärtstore, quoten);
	}
	
	public String toString() {
		return super.toString() +" (festgelegtes Ergebnis)"; 
	}
}
