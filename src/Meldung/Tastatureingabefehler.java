package Meldung;

/**
 * Eine Fehlermeldung, wenn mit der Eingabe durch die Tastatur ein Fehler erzeugt wird
 * @author Attila Kiss
 */
public class Tastatureingabefehler extends Fehler{
	
	private static final long serialVersionUID = 5;

	/**
	 * Erzeugt eine Fehlermeldung, wenn mit der Eingabe durch die Tastatur ein Fehler erzeugt wird
	 * @param fehlerspezifikation
	 */
	public Tastatureingabefehler(String fehlerspezifikation) {
		super("Tastatureingabefehler : "+fehlerspezifikation);
	}

}
