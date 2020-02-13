package Fussball.Spielobjekte;

import Allgemein.Verwendbare;
import Baumwurzelstruktur.Wurzelstruktur;
import Fussball.Chronologisch.Tageszeit;
import Meldung.Wertangabefehler;

/**
 * Enthält ein Spiel mit Heim- und Auswärtsteam und diversen andere Daten
 * @author Attila Kiss
 */
public abstract class Spiel extends Tageszeit<Spiel> {
	
	public final String heimteam, auswärtsteam;
	
	// TODO Quoten als vergleichbares Objekt
	// Quoten
	/**	QVK = Quote vor Komma */
	public final short heimsiegQVK, remisQVK, auswärtssiegQVK;
	/** QNK = Quote nach Komma */
	public final byte heimsiegQNK, remisQNK, auswärtssiegQNK;

	/** 
	 * 22 ist die maximale Anzahl an Buchstaben, die für einen Vereinsnamen vorgesehen ist. Es wird bei der Ausgabe benötigt und kann davor angepasst werden.
	 */
	public static byte längsterName = 22;
	
 	Spiel (byte[] terminteile, String heimteam, String auswärtsteam, short[] quotenteile) throws Wertangabefehler {
		super (terminteile[0], terminteile[3], terminteile[4], terminteile[5]);
		this.heimteam = heimteam;
		this.auswärtsteam = auswärtsteam;
		heimsiegQVK = quotenteile[0];
		remisQVK = quotenteile[2];
		auswärtssiegQVK = quotenteile[4];
		heimsiegQNK = (byte) quotenteile[1];
		remisQNK = (byte) quotenteile[3];
		auswärtssiegQNK = (byte) quotenteile[5];
	}
	
 	
	public int compareTo (Spiel ander) {
		int rückgabewert = super.compareTo(ander);
		if (rückgabewert==0)
			rückgabewert = heimteam.compareTo(ander.heimteam);
		if (rückgabewert==0)
			rückgabewert = auswärtsteam.compareTo(ander.auswärtsteam);
		return rückgabewert;
	}
	
	public String toString() {
		return super.toString() +"   " +heimteam  +Verwendbare.leerzeichen (heimteam, längsterName) +" - " +Verwendbare.leerzeichen (auswärtsteam, längsterName) +auswärtsteam;	
	}
	
	/**
	 * @return true, wenn exakt die gleichen Heim- und Auswärtsteams gegeneinander spielen.
	 * @param ander ist das andere Spiel
	 */
	public boolean exaktGleicheTeams (Spiel ander) {
		if (heimteam.equals(ander.heimteam) && auswärtsteam.equals(ander.auswärtsteam))
			return true;
		return false;
	}
	
	public void teamnamenaufnahme (Wurzelstruktur<String> teamnamen) {
		teamnamen.verwurzel(heimteam);
		teamnamen.verwurzel(auswärtsteam);
	}

	public boolean rückspiel (Spiel spiel) {
		if (this.heimteam.equals(spiel.auswärtsteam) && this.auswärtsteam.equals(spiel.heimteam))
			return true;
		return false;
	}
	
	/**
	 * Gibt an, ob dieses Spiel und das andere Spiel einer der beiden Teams gemein haben. Wenn ja ist das eine Spiel das darauf folgende Spiel des anderen.
	 * Dabei gibt der Rückgabewert genauere Auskunft darüber welcher Team das folgende Spiel hat.
	 * @param spiel
	 * @return 0 wenn kein Folgespiel, 1 wenn das Heimteam und 2 wenn das Auswärtteam dieses Spieles mit einem Team des anderen Spieles übereinstimmt 
	 */
	public byte folgeSpiel (Spiel spiel) {
		byte code;
		if ((code = gefundenesTeam (spiel.heimteam)) != 0)
			return code;
		if ((code = gefundenesTeam (spiel.auswärtsteam)) != 0)
			return code;
		return 0;
	}
	
	/**
	 * @return 0 wenn kein Team im Spiel teilnimmt, 1 wenn das Heimteam und 2 wenn das Auswärtsteam dieses Spieles mit dem Teamnamen übereinstimmt 
	 * @param name des gesuchten Teams
	 */
	public byte gefundenesTeam (String name) {
		if (name.equals(heimteam))
			return 1;
		if (name.equals(auswärtsteam))
			return 2;
		return 0;
	}
	
	/**
	 * Gibt an, ob das Spiel über die reguläre Spielzeit von 90 Min. ging (Verlängerung oder Elfmeterschießen) oder nicht
	 * @param spiel
	 * @return true, wenn das Spiel kein reguläres Spiel war
	 */
	public boolean spielÜ90() {
		if (getClass().getSuperclass().getSimpleName().equals("ReguläresSpiel"))								// Verlängerte Spiele
			return true;
		return false;
	}
	
	/** @return Gibt an, ob das Spiel nur gesetzt worden ist und somit noch nicht stattfand oder nicht */
	public boolean gesetztesSpiel() {
		if (getClass().getSimpleName().equals("GesetztesSpiel"))
			return true;
		return false;
	}
	
	/** @return Gibt an, ob der Sieger des Spiels festgelegt worden ist, wobei das Ergebnis 0:0 bleibt. */
	public boolean festgelegterSieg() {
		if (getClass().getSimpleName().equals("FestgelegterHeimsieg") || getClass().getSimpleName().equals("FestgelegterAuswärtssieg"))
			return true;
		return false;
	}
	
	/**
	 * @return true, wenn es zum Elfmeterschießen gekommen ist
	 */
	public boolean elfer() {
		if (this instanceof SpielME || this instanceof VerlängertesSpielME)
			return true;
		return false;
	}
	
	/**
	 * @return true, wenn es Quoten zum Spiel gibt
	 */
	public boolean vorhandeneQuoten() {
		if (heimsiegQVK==0 && remisQVK==0 && auswärtssiegQVK==0)
			return false;
		return true;
	}
	
	public String quotenText() {
		if (vorhandeneQuoten())
			return heimsiegQVK +"." +heimsiegQNK +" " +remisQVK +"." +remisQNK +" " +auswärtssiegQVK +"." +auswärtssiegQNK;
		return "Keine Quoten vorhanden.";
	}
}
