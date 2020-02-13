package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel ohne Ergebnisse
 * @author Attila Kiss
 */
public class GesetztesSpiel extends Spiel {

	/**
	 * Erzeugt ein Spiel ohne Ergebnisse
	 * @param terminteile
	 * @param heimteam
	 * @param auswärtsteam
	 * @param quotenteile sind je 3 Vor- und Nachkommastellenangaben für 1-X-2. Wenn keine Quoten vorhanden sind, steht überall -1.
	 * @throws Wertangabefehler
	 */
	public GesetztesSpiel (byte[] terminteile, String heimteam, String auswärtsteam, short[] quotenteile) throws Wertangabefehler {	
		super (terminteile, heimteam, auswärtsteam, quotenteile);
	}
}
