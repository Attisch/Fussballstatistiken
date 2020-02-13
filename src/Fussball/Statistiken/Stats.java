package Fussball.Statistiken;

import Fussball.Spielobjekte.ReguläresSpiel;
import Meldung.Fehler;
import Meldung.Wertangabefehler;

/**
 * Statistik über Tore und Siege
 * @author Attila Kiss
 */
public class Stats {

	/**
	 * Liste mit Spielen die nach deren Häufigkeitsangaben der gefallenen Tore eingeteilt worden sind. Die Häufigkeit erhöht sich nicht nur bei genauer Übereinstimmung,
	 * sondern auch wenn mindestens so viele Tore wie angegeben gefallen sind.
	 */
	public int[] mindestTore = new int[10];				// Mindestens 1,2,3,...,10 (oder mehr als 10) Tore
	public int[] heimsiegMitHc = new int[10];			// 0:0, 0:1, 0:2, 0:3,...,0:9 (oder mehr als 9) Tore
	public int[] auswärtssiegMitHc = new int[10];		// 0:0, 1:0, 2:0, 3:0,...,9:0 (oder mehr als 9) Tore

	
	/** Anzahl der Spiele, aus denen die Torangaben stammen */
	public int spielanzahl = 0;

	public Stats() {}

	public String toString() {
		String text;
		if (spielanzahl==0)
			text = "Keine Spiele für eine Statistik gesammelt.";
		else {
			text = "Von insgesamt " +spielanzahl +" ausgewerteten Spielen:\n\nTorstatistik:\n";
			byte prozentzahl = 0;
			try {
				for (byte b = 0; b< mindestTore.length; b++) {
					prozentzahl = Allgemein.Verwendbare.prozent (mindestTore[b], spielanzahl);
					text += "Mindestens " +(1+b) +" Tore in " +mindestTore[b] +" Spielen bzw. zu "+prozentzahl +"%, \t\t also höchstens " +b +" Tore in " +(spielanzahl-mindestTore[b]) +" Spiele bzw. zu " +(100-prozentzahl) +"%\n";
					if (mindestTore[b]==0)
						break;
				}
				int summe = heimsiegMitHc[0] +auswärtssiegMitHc[0];
				prozentzahl = Allgemein.Verwendbare.prozent (summe, spielanzahl);
				text += "\nSieg in " +summe +" Spielen bzw. zu " +prozentzahl +"%, \t\tUnentschieden in " +(spielanzahl-summe) +" Spielen bzw. zu " +(100-prozentzahl) +"%\n";
				text += siegstatsText ("Heimsieg", heimsiegMitHc);
				text += siegstatsText ("Auswärtssieg", auswärtssiegMitHc);
			} catch (Wertangabefehler e) {
				Fehler.entstehung(e);
			}
		}
		return text;	
	}
	
	private String siegstatsText (String titel, int[] stats) throws Wertangabefehler {
		String text = "";
		if (stats[0]!=0) {
			text += "\n" +titel +"\t\t\t\t\t\t\t\tUnentschieden\n";
			for (byte b = 0; b< stats.length; b++) {
				if (stats[b]==0)
					break;
				text += "mit mindestens " +(1+b) +" Toren Unterschied in " +stats[b] +" Spielen bzw. zu "+Allgemein.Verwendbare.prozent (stats[b], spielanzahl) +"%, \t\t";
				if (b+1< stats.length) {
					int diff = stats[b] -stats[b+1];
					text += "mit genau " +(1+b) +" Toren Unterschied in " +diff +" Spielen bzw. zu " +Allgemein.Verwendbare.prozent (diff, spielanzahl) +"%\n";
				} else text += "\n";
			}
		}
		else text += "\nKeine " +titel +"e.\n";
		return text;
	}

	/**
	 * Aktualisiert die Torstatistik mit der Anzahl der angegebenen Toren
	 * @param heimtore
	 * @param auswärtstore
	 */
	public void ergänze (byte heimtore, byte auswärtstore) {
		spielanzahl++;
		
		// Torstatistik
		for (byte b = 0; b< mindestTore.length; b++) {
			if (heimtore +auswärtstore >b)
				mindestTore[b]++;
			else break;
		}
		
		// Heimsieg mit Handycapstatistik
		for (byte b = 0; b< heimsiegMitHc.length; b++) {
			if (heimtore -auswärtstore -b >0)
				heimsiegMitHc[b]++;
			else break;
		}
		
		// Auswärtssieg mit Handycapstatistik
		for (byte b = 0; b< auswärtssiegMitHc.length; b++) {
			if (auswärtstore -heimtore -b >0)
				auswärtssiegMitHc[b]++;
			else break;
		}
	}

	/**
	 * Aktualisiert die Torstatistik mit der Anzahl der Toren des Spiels
	 * @param spiel
	 */
	public void ergänze (ReguläresSpiel spiel) {
		ergänze(spiel.heimtore ,spiel.auswärtstore);
	}
	
	public void kopiereNach (Stats stats) {
		stats.spielanzahl = spielanzahl;
		stats.mindestTore = mindestTore;
		stats.heimsiegMitHc = heimsiegMitHc;
		stats.auswärtssiegMitHc = auswärtssiegMitHc;
	}
}
