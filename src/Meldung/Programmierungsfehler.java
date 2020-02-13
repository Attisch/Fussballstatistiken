package Meldung;

/**
 * Eine Fehlermeldung, wenn bei der Programmierung was vergessen wurde, das zu Fehlern führt
 * @author Attila Kiss
 */
public class Programmierungsfehler extends Fehler {

	private static final long serialVersionUID = 7;

	/**
	 * Erzeugt eine Fehlermeldung, wenn bei der Programmierung was vergessen wurde, das zu Fehlern führt
	 * @param fehlerspezifikation genauere Beschreibung des Fehlers
	 */
	public Programmierungsfehler (String fehlerspezifikation) {
		super ("Falsch programmiert: " +fehlerspezifikation);
	}

}
