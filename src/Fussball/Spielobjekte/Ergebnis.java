package Fussball.Spielobjekte;

import Allgemein.Verwendbare;

/**
 * Enthält die Optionen des Spielausgangs: Unbekannt (falls das Spiel noch nicht stattfand), Sieg, Remis, Niederlage
 * @author Attila Kiss
 *
 */
public enum Ergebnis {
	UNBEKANNT, SIEG, REMIS, NIEDERLAGE;
	
	public String toString() {
		String wort = super.toString();
		return Verwendbare.zuNomen(wort);
	}
	
	/**
	 * @return Gibt die Enumkonstante als Wort in Mehrzahl zurück.
	 */
	public String mehrzahl() {
		String wort = toString();
		if (this==Ergebnis.SIEG) wort = "Siege";
		else if (this==Ergebnis.NIEDERLAGE) wort = "Niederlagen";
		return wort;
	}
}

