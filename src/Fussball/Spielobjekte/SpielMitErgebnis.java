package Fussball.Spielobjekte;

import Allgemein.Verwendbare;
import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel, dessen Endergebnis aber nicht dessen Halbzeitergebnis vorliegt
 * @author Attila Kiss
 */
public abstract class SpielMitErgebnis extends GesetztesSpiel {
	
	public final byte heimtore, auswärtstore;

	/**
	 * Erzeugt ein festgelegtes Spiel, dessen Ergebnis vorliegt.
	 * @author Attila Kiss
	 */
	public SpielMitErgebnis (byte[] terminteile, String heimteam, String auswärtsteam, byte heimtore, byte auswärtstore, short[] quoten) throws Wertangabefehler {
		super (terminteile, heimteam, auswärtsteam, quoten);
		this.heimtore = heimtore;
		this.auswärtstore = auswärtstore;
	}
	
	public String toString() {
		String[] textteile = super.toString().split("-", 2);
		return textteile[0] +Verwendbare.zahlformat(heimtore, 2, true) +":" +Verwendbare.zahlformat(auswärtstore, 2, false) +textteile[1];
	}
	
	/**
	 * Gibt das Ergebnis des Rückspieles nach 90 min (incl. Nachspielzeit) zurück. 0.Stelle = Heimtore, 1.Stelle = Auswärtstore
	 * @return liste mit heimtoren- und auswärtstorenanzahl
	 */
	public byte[] spiel90 (SpielMitErgebnis hinspiel) {
		byte[] ergebnis = new byte[2];
		if (spielÜ90()) {
			ergebnis[0] = hinspiel.auswärtstore;
			ergebnis[1] = hinspiel.heimtore;
		} else {
			ergebnis[0] = heimtore;
			ergebnis[1] = auswärtstore;
		}
		return ergebnis;
	}
	
	/**
	 * @return Gibt das {@link Ergebnis} aus der Sicht des Heimteams zurück.
	 */
	public Ergebnis ergebnisHeimsicht() {
		if (heimtore >auswärtstore)
			return Ergebnis.SIEG;
		if (heimtore< auswärtstore)
			return Ergebnis.NIEDERLAGE;
		return Ergebnis.REMIS;
	}
	
	/**
	 * @return Name des Teams, der das Spiel für sich entscheiden konnte oder null
	 */
	public String sieger() {
		Ergebnis erg = ergebnisHeimsicht();
		if (erg==Ergebnis.SIEG)
			return heimteam;
		if (erg==Ergebnis.NIEDERLAGE)
			return auswärtsteam;
		return null;
	}
	
	/**
	 * @return Name des Teams, der das Spiel verloren hat oder null
	 */
	public String verlierer() {
		String sieger = sieger();
		if (sieger==null)
			return null;
		if (sieger.equals(heimteam))
			return auswärtsteam;
		return heimteam;
	}
}
