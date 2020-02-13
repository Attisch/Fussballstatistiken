package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
 * Enthält ein verschobenes Spiel. 
 * @author Attila Kiss
 */
public final class VerschobenesSpiel extends GesetztesSpiel implements NeuZuSetzendesSpiel {
	
	/**
	 * Erzeugt ein verschobenes Spiel, das neu gesetzt werden mussdas einen Link zum neu gesetzten Spiel hat.
	 * @param heimteam
	 * @param auswärtsteam
	 * @param quotenteile sind je 3 Vor- und Nachkommastellenangaben für 1-X-2. Wenn keine Quoten vorhanden sind, steht überall -1.
	 * @throws Wertangabefehler 
	 */
	public VerschobenesSpiel (byte[] terminteile, String heimteam, String auswärtsteam, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, quoten);
	}
	
	public String toString() {
		return super.toString() +" (Verschoben)";
	}

}
