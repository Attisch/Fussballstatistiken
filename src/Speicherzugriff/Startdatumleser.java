package Speicherzugriff;

import Meldung.Formatfehler;
import Meldung.Wertangabefehler;

/**
 * Ist eine {@link Ladeprozedur}, die dabei hilft die Startdatum.csv-Datei einzulesen, um deren Daten zu erhalten
 * @see Datei#ladeprozedere
 * @author Attila Kiss
 */
public class Startdatumleser implements Ladeprozedur {

	public byte khwNr;
	public byte[] terminteile = new byte[4];
	private boolean ersteZeile;
	
	/**
	 * Erzeugt ein Object, das die Daten der Startdatum.csv-Datei durch Einlesen erhalten kann
	 */
	public Startdatumleser() {}

	public void ende() {
		ersteZeile = false;
	}
	
	public Boolean benutzt (String zeile) throws Formatfehler, Wertangabefehler {
		if (!ersteZeile)
			ersteZeile = true;
		else {
			String[] zeilenteile = zeile.split(";");
			if (zeilenteile.length !=3)
				throw new Formatfehler ("Das Startdatumformat ist ungültig: " +zeile);
			Datei.terminteile (zeilenteile, terminteile);
			try {
				khwNr = Byte.parseByte (zeilenteile[2]);
			} catch (NumberFormatException e) {
				throw new Formatfehler ("Die Kalenderhalbwochennummer konnte nicht aus der Zeile gelesen werden: " +zeile);		
			}
		}
		return true;
	}

	public String datei() {
		return "src/Fussball/Chronologisch/Startdatum.csv";
	}
}
