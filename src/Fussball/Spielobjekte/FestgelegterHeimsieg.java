package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein festgelegtes Spiel, als dessen Sieger die Heimmannschaft bestimmt wurde.
 * @author Attila Kiss
 */
public final class FestgelegterHeimsieg extends GesetztesSpiel implements FesterSieger, FestgelegterSieger {

	/**
	 * Erzeugt ein festgelegtes Spiel, als dessen Sieger die Heimmannschaft bestimmt wurde.
	 * @param terminteile
	 * @param heimteam
	 * @param auswärtsteam
	 * @param quoten
	 * @throws Wertangabefehler 
	 */
	public FestgelegterHeimsieg (byte[] terminteile, String heimteam, String auswärtsteam, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, quoten);
	}
	
	public String toString() {
		return super.toString() +" (Festgelegter Heimsieg)";
	}

	public String sieger() {
		return heimteam;
	}

	public String verlierer() {
		return auswärtsteam;
	}	
}
