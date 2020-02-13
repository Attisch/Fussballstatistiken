package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel mit Verlängerung und Elfmeterschießen
 * @author Attila Kiss
 */
public class VerlängertesSpielME extends ReguläresSpiel {
	
	public VerlängertesSpielME (byte[] terminteile, String heimteam, String auswärtsteam, byte[] tore, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, tore, quoten);
	}
	
	public String toString() {
		return super.toString() +" (n.E.)";
	}
}
