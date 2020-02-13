package Fussball.Statistiken;

import Fussball.Spielobjekte.ReguläresSpiel;

/**
 * @author Attila Kiss
 */
public class StatsNachHzErgebnis extends StatsNachErgebnis<ReguläresSpiel>{

	public StatsNachHzErgebnis (byte heimtore, byte auswärtstore) {
		super (heimtore, auswärtstore);
	}
	
	public String toString() {
		return "\nStatistik der 2. Halbzeit in Bezug zum Halbzeitergebnis von " +heimtore +":" +auswärtstore +"\n" +super.toString();
	}
	
}
