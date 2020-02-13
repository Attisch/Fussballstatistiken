package Meldung;

/**
 * Enthält eine Fehlermeldung und ein serialVersionUID, wenn Probleme mit dem Verzeichnis aufreten.
 * @author Attila Kiss
 */
public final class Verzeichnisfehler extends Fehler {
	
	private static final long serialVersionUID = 6;

	/**
	 * Erzeugt eine Fehlermeldung und ein serialVersionUID, wenn Probleme mit dem Verzeichnis aufreten.
	 * @param verzeichnis mit Pfadangabe
	 * @param fehlerspezifikation
	 */
	public Verzeichnisfehler (String verzeichnis, String fehlerspezifikation) {
		super (verzeichnis+". "+fehlerspezifikation);
	}

}
