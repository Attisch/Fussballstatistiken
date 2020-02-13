 package Speicherzugriff;

import Fussball.Spielobjekte.*;
import Meldung.Formatfehler;
import Meldung.Wertangabefehler;

/**
 * Enthält eine Methode die ein Spiel mit dem richtigen Format wiedergibt.
 * @author Attila Kiss
 */
public final class Spielwahl {

	private Spielwahl() {}
	
	/**
	 * Gibt ein Spiel mit dem richtigen Format zurück
	 * @param terminteile bekommt die Daten von {@link Datei#terminteile Terminteile} übergeben
	 * @param zeilenteile der Spielzeile
	 * @return die Spieldaten mit oder ohne Ergebnisse
	 * @throws Formatfehler in der Datei
	 * @throws Wertangabefehler bei der Erstellung des Spiels
	 */
	static Spiel lade (byte[] terminteile, String[] zeilenteile) throws Formatfehler, Wertangabefehler {
		byte[] tore = Datei.ergebnisprüfung (zeilenteile, 5);
		String ereignis = Datei.ereignisprüfung (zeilenteile[7]);
		short[] quoten = Datei.quotenprüfung(zeilenteile);
		final byte keinErgebnis = Allgemein.Verwendbare.UNBEKANNT;

		if (tore[2]==keinErgebnis)	{														// kein Ergebnis
			if (ereignis==null)
				return new GesetztesSpiel (terminteile, zeilenteile[3], zeilenteile[4], quoten);
			if (ereignis.equals("vS"))
				return new VerschobenesSpiel (terminteile, zeilenteile[3], zeilenteile[4], quoten);
			if (ereignis.equals("fH"))
				return new FestgelegterHeimsieg (terminteile, zeilenteile[3], zeilenteile[4], quoten);
			return new FestgelegterAuswärtssieg (terminteile, zeilenteile[3], zeilenteile[4], quoten);
		} else {
			if (tore[0]==keinErgebnis)														// Wenn das Ergebnis, aber kein Halbzeitergebnis vorliegt
				return new FestgelegtesErgebnis (terminteile, zeilenteile[3], zeilenteile[4], tore[2], tore[3], quoten);
			else if (ereignis==null)
				return new ReguläresSpiel (terminteile, zeilenteile[3], zeilenteile[4], tore, quoten);
			else if (ereignis.equals("nV"))
				return new VerlängertesSpiel (terminteile, zeilenteile[3], zeilenteile[4], tore, quoten);
			else if (ereignis.equals("nE"))
				return new SpielME (terminteile, zeilenteile[3], zeilenteile[4], tore, quoten);
			else return new VerlängertesSpielME (terminteile, zeilenteile[3], zeilenteile[4], tore, quoten);
		}
	}
}
