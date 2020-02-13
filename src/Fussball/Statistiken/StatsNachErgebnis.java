package Fussball.Statistiken;

import java.util.ArrayList;

/**
 * Ergebnisorientierte Statistik der Spiele, deren Typ extra zu bestimmen ist.
 * @param <T> Typ der Spiele
 * 
 * @author Attila Kiss
 */
abstract class StatsNachErgebnis<T> extends Stats {

	public final byte heimtore, auswärtstore;					// Falls mal die Spieleliste gelöscht wird, weil zu viele Spiele nicht von Gebrauch sind
	public final ArrayList<T> spieleliste = new ArrayList<>(); 

	public StatsNachErgebnis (byte heimtore, byte auswärtstore) {
		this.heimtore = heimtore;
		this.auswärtstore = auswärtstore;
	}

	public String toString() {
		String text = "";
		if (!spieleliste.isEmpty()) {
			text = "Spiele der Statistik;\n";
			for (T es : spieleliste)
				text += es.toString() +"\n";
		}
		text += super.toString();
		return text;
	}
}