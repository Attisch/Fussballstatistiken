package Fussball.Chronologisch;

import Allgemein.Verwendbare;
import Meldung.Wertangabefehler;

/**
 * Enthält ergänzend zum {@link Tag} die Tageszeit mit Stunden- und Minutenangabe
 * @param <T> Typ des Objekts, das die Tageszeit mit Daten erweitert (Für den Vergleichsoperator "Comparable" zu bestimmen)
 * @author Attila Kiss
 */
public abstract class Tageszeit<T extends Tageszeit<T>> extends Tag<T> {

	byte stunde, minute;
	
	protected Tageszeit() {}
	
	/**
	 * Ohne Uhrzeitangabe
	 * @throws Wertangabefehler 
	 */
	protected Tageszeit (byte kalendertag, byte wochentag) throws Wertangabefehler {
		super (kalendertag, wochentag);
		stunde = Verwendbare.UNBEKANNT;
		minute = Verwendbare.UNBEKANNT;
	}
	
	/**
	 * Für die aktuelle Zeitinitialisierung vorgesehen
	 * @throws Wertangabefehler 
	 */
	protected Tageszeit (byte kalendertag, byte wochentag, byte stunde, byte minute) throws Wertangabefehler {
		super (kalendertag, wochentag);
		uhrzeitzuweisung (stunde, minute);
	}
	
	// Getter
	public final byte stunde() { return stunde; }
	public final byte minute() { return minute; }
	
	/**
	 * Werte werden vor der Zuweisung geprüft
	 * @param stunde
	 * @param minute
	 * @throws Wertangabefehler
	 */
	void uhrzeitzuweisung (byte stunde, byte minute) throws Wertangabefehler {
		Verwendbare.wertprüfung (stunde, Verwendbare.UNBEKANNT, 23, "Falsche Stundenangabe");
		Verwendbare.wertprüfung (minute, Verwendbare.UNBEKANNT, 59, "Falsche Minutenangabe");
		this.stunde = stunde;
		this.minute = minute;
	}
	
	public String toString() {
		String text = super.toString();
		if (stunde==Verwendbare.UNBEKANNT)
			return text +" ??:?? Uhr";
		return text +" " +Verwendbare.nullformatText(stunde) +":" +Verwendbare.nullformatText(minute);
	}
	
	public int compareTo (T ander) {
		int rückgabewert = super.compareTo(ander);
		return rückgabewert !=0 ? rückgabewert : stunde==ander.stunde && minute==ander.minute ? 0 : stunde< ander.stunde || (stunde==ander.stunde && minute< ander.minute) ? -1 : 1;
	}
}
