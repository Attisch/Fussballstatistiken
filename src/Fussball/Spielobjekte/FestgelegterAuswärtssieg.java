package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein festgelegtes Spiel, als dessen Sieger die Auswärtsmannschaft bestimmt wurde.
 * @author Attila Kiss
 */
public final class FestgelegterAuswärtssieg extends GesetztesSpiel implements FesterSieger, FestgelegterSieger {

	/**
	 * Erzeugt ein festgelegtes Spiel, als dessen Sieger die Auswärtsmannschaft bestimmt wurde.
	 * @param terminteile
	 * @param heimteam
	 * @param auswärtsteam
	 * @param quoten
	 * @throws Wertangabefehler 
	 */
	public FestgelegterAuswärtssieg (byte[] terminteile, String heimteam, String auswärtsteam, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, quoten);
	}
	
	public String toString() {
		return super.toString() +" (Festgelegter Auswärtssieg)";
	}

	public String sieger() {
		return auswärtsteam;
	}

	public String verlierer() {
		return heimteam;
	}	
}
