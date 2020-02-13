package Fussball.Spielobjekte;

import Allgemein.Verwendbare;
import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel mit regulärer Spielzeit
 * @author Attila Kiss
 */
public class ReguläresSpiel extends SpielMitErgebnis implements FesterSieger {

	public final byte heimtoreHz, auswärtstoreHz;
	
	public ReguläresSpiel (byte[] terminteile, String heimteam, String auswärtsteam, byte[] tore, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, tore[2], tore[3], quoten);
		this.heimtoreHz = tore[0];
		this.auswärtstoreHz = tore[1];
	}
	
	public String toString() {
		return super.toString() +"   (Hz "+Verwendbare.zahlformat(heimtoreHz, 2, true) +":" +Verwendbare.zahlformat(auswärtstoreHz, 2, false) +")";
	}
}
