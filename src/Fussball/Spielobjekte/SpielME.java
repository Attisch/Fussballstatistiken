package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel mit Elfmeterschießen (ohne Verlängerung)
 * @author Attila Kiss
 */
public class SpielME extends ReguläresSpiel {
	
	public SpielME (byte[] terminteile, String heimteam, String auswärtsteam, byte[] tore, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, tore, quoten);
	}
	
	public String toString() {
		return super.toString() +" (n.E.)";
	}
}
