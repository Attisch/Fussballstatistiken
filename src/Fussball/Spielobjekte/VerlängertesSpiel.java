package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel mit Verlängerung
 * @author Attila Kiss
 */
public class VerlängertesSpiel extends ReguläresSpiel {
	
	public VerlängertesSpiel (byte[] terminteile, String heimteam, String auswärtsteam, byte[] tore, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, tore, quoten);
	}
	
	public String toString() {
		return super.toString() +" (n.V.)";
	}
}
