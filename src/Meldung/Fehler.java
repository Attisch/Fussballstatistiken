package Meldung;

/**
 * Allgemeiner Fehler, der ebenfalls eine Methode zur abschließender Fehlerausgabe beinhaltet
 * @author Attila Kiss
 */
public class Fehler extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0;


	public Fehler (String fehlerspezifikation) {
		super (fehlerspezifikation);
	}
	
	/**
	 * @return Pfadangabe der Fehlerentstehung bis zum Aufkommen des Methodennamens inklusive
	 * @param e ist der Fehler
	 * @param methodenpfad ist der Methodenname samt Pfad. Er dient zur Erkennung der Abbruchstelle, an der die Pfadangabe der Fehlerentstehung nicht mehr weiter einliest bzw. bis dahin die Pfadangabe als Text zurückgibt.
	 */
	public static String pfadteilangabe (Exception e, String methodenpfad) {
		StackTraceElement[] fehlerpfad = e.getStackTrace();
		String text = e.toString();
		String meldung;
		for (StackTraceElement s : fehlerpfad) {
			meldung = s.toString();
			text += "\n"+meldung;
			if (meldung.startsWith(methodenpfad))
				break;
		}
		return text;		
	}
	
	/**
	 * @return Die Fehlermeldung und den Entstehungspfad
	 * @param e wie exception
	 */
	public static String entstehung (Exception e) {
		return pfadteilangabe(e, "#");
	}
}
