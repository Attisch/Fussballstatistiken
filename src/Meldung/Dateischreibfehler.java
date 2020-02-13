package Meldung;

/**
 * Enthält einen Fehler, wenn eine Datei nicht ordnungsgemäß geschrieben werden konnte.
 * @author Attila Kiss
 */
public class Dateischreibfehler extends Fehler {

	private static final long serialVersionUID = 4;

	/**
	 * Erzeugt einen Fehler, wenn eine Datei nicht ordnungsgemäß geschrieben werden konnte.
	 * @param Dateiname mit Pfad
	 * @param fehlerspezifikation gibt eine genauere Bezeichung der Fehlerquelle an
	 */
	public Dateischreibfehler(String dateinameMitPfad, String fehlerspezifikation) {
		super("Fehler beim Schreiben der Datei: "+dateinameMitPfad+". "+fehlerspezifikation);
	}

}
