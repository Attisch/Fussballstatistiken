package Meldung;

/**
 * Enthält eine Fehlermeldung und ein serialVersionUID, wenn eine Angabe einen unmöglichen Wert hat.
 * @author Attila Kiss
 */
public final class Wertangabefehler extends Fehler{

	private static final long serialVersionUID = 1;

	/**
	 * Erzeugt einen Fehler, wenn eine Angabe einen unmöglichen Wert hat
	 * @param fehlerspezifikation gibt eine genauere Bezeichung der Fehlerquelle an
	 */
	public Wertangabefehler(String fehlerspezifikation) {
		super ("Unmöglicher Wert: " +fehlerspezifikation);
	}

}
