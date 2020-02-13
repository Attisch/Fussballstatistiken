package Meldung;

/**
 * Enthält einen Fehler, wenn eine Datei nicht ordnungsgemäß eingelesen werden konnte.
 * @author Attila Kiss
 */
public class Dateilesefehler extends Fehler{
	
	private static final long serialVersionUID = 3;

	/**
	 * Erzeugt einen Fehler, wenn eine Datei nicht ordnungsgemäß eingelesen werden konnte.
	 * @param fehlerspezifikation gibt eine genauere Bezeichung der Fehlerquelle an
	 */
	public Dateilesefehler(String dateinameMitPfad, String fehlerspezifikation) {
		super("Aktuelle Datei: " +dateinameMitPfad +". " +fehlerspezifikation);
	}

}
