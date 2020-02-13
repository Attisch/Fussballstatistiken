package Meldung;

/**
 * Eine Fehlermeldung, wenn die Datei zum Einlesen ein unerwartetes Format hat
 * @author Attila Kiss
 */
public class Formatfehler extends Fehler {

	private static final long serialVersionUID = 2;

	/**
	 * Erzeugt eine Fehlermeldung, wenn die Datei zum Einlesen ein unerwartetes Format hat.
	 * @param fehlerspezifikation genauere Beschreibung des Fehlers
	 */
	public Formatfehler (String fehlerspezifikation) {
		super (fehlerspezifikation);
	}

}
