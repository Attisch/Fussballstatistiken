package Fussball;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Attila Kiss
 */
public class Tabelle {
	
	public final Tabellenplatz[] plätze;

	public Tabelle (byte maxPlätze) {
		plätze = new Tabellenplatz[maxPlätze];
	}
	
	public void sortieren() {
		Arrays.sort (plätze, PunkteTorDiffVergleicher);
	}
	
	/**
	 * Vergleicht die Punkte und Tordifferenzen der zwei Teams in der Tabelle
	 */
	private static Comparator<Tabellenplatz> PunkteTorDiffVergleicher = new Comparator<Tabellenplatz>() {
		public int compare (Tabellenplatz platz1, Tabellenplatz platz2) {
			return platz1.compareTo(platz2);
		}
	};

}
