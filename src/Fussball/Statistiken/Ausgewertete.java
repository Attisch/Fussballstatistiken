package Fussball.Statistiken;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Allgemein.Verwendbare;
import Fussball.Wettbewerb;
import Fussball.Chronologisch.Zeitpunkt;
import Meldung.Fehler;
import Meldung.Wertangabefehler;
import Programme.Starter;
import Speicherzugriff.Datei;
import Visualität.Ausgabe;

/**
 * Enthält alle verfügbaren ausgewerteten Statistiken
 * @author Attila Kiss
 */
public class Ausgewertete {

	private Ausgewertete() {}
		

	private static Comparator<Stats> kategorieNachSpielanzahl = new Comparator<Stats>() {
		public int compare(Stats kat1, Stats kat2) {
			return kat1.spielanzahl < kat2.spielanzahl ? 1 : kat1.spielanzahl==kat2.spielanzahl ? 0 : -1;
		}
	};
	
	private static Comparator<Stats> kategorieNachMind2Toren = new Comparator<Stats>() {
		public int compare(Stats kat1, Stats kat2) {
			byte prozentzahl1 = -1, prozentzahl2 = -1;
			try {
				prozentzahl1 = Allgemein.Verwendbare.prozent (kat1.mindestTore[1], kat1.spielanzahl);
				prozentzahl2 = Allgemein.Verwendbare.prozent (kat2.mindestTore[1], kat2.spielanzahl);
			} catch (Wertangabefehler e) {
				Visualität.Ausgabe.instanz.text(Fehler.entstehung(e));
			}
			return prozentzahl1 < prozentzahl2 ? 1 : prozentzahl1==prozentzahl2 ? 0 : -1;
		}
	};
	
	private static int kategorieToreUndSieg (Stats kat1, Stats kat2, int mindToreIndex, int maxToreIndex) {
		byte[] prozentzahlen1 = new byte[3], prozentzahlen2 = new byte[3];
		byte grösste1 = 0, grösste2 = 0;
		if (kat1.spielanzahl==0)
			return 1;
		if (kat2.spielanzahl==0)
			return 2;
		try {
			prozentzahlen1[0] = Allgemein.Verwendbare.prozent (kat1.mindestTore[mindToreIndex], kat1.spielanzahl);
			prozentzahlen2[0] = Allgemein.Verwendbare.prozent (kat2.mindestTore[mindToreIndex], kat2.spielanzahl);
			prozentzahlen1[1] = (byte) (100 -Allgemein.Verwendbare.prozent (kat1.mindestTore[maxToreIndex], kat1.spielanzahl));
			prozentzahlen2[1] = (byte) (100 -Allgemein.Verwendbare.prozent (kat2.mindestTore[maxToreIndex], kat2.spielanzahl));
			int summe = kat1.heimsiegMitHc[0] +kat1.auswärtssiegMitHc[0];
			prozentzahlen1[2] = Allgemein.Verwendbare.prozent (summe, kat1.spielanzahl);
			summe = kat2.heimsiegMitHc[0] +kat2.auswärtssiegMitHc[0];
			prozentzahlen2[2] = Allgemein.Verwendbare.prozent (summe, kat2.spielanzahl);
			grösste1 = Verwendbare.grössteZahl (prozentzahlen1);
			grösste2 = Verwendbare.grössteZahl (prozentzahlen2);
		} catch (Wertangabefehler e) {
			Visualität.Ausgabe.instanz.text ("Fehler im Comperator der Kategorie '" +(mindToreIndex+1) +" bis " +(maxToreIndex+1) +" Tore': " +Fehler.entstehung(e));
		}
		return grösste1< grösste2 ? 1 : grösste1==grösste2 ? 0 : -1;
	}
	
	/**
	 * Vegleicht die Wahrscheinlichkeit des Eintreffens von mind. 1 Toren und von max. 2 Toren sowie von Sieg. Orientiert sich nach der größten derer.
	 */
	private static Comparator<Stats> kategorie1_2ToreUndSieg = new Comparator<Stats>() {
		public int compare (Stats kat1, Stats kat2) {
			return kategorieToreUndSieg (kat1, kat2, 0, 2);
		}
	};	
	
	/**
	 * Vegleicht die Wahrscheinlichkeit des Eintreffens von mind. 2 Toren und von max. 3 Toren sowie von Sieg. Orientiert sich nach der größten derer.
	 */
	private static Comparator<Stats> kategorie2_3ToreUndSieg = new Comparator<Stats>() {
		public int compare (Stats kat1, Stats kat2) {
			return kategorieToreUndSieg (kat1, kat2, 1, 3);
		}
	};

	public static void hzEndstandStats() throws Fehler {
		HzEndergebnisstatistiker stats = new HzEndergebnisstatistiker();
		ArrayList<File> dateien = new ArrayList<>((Zeitpunkt.aktuell.khw.jahreszahl -Verwendbare.MIN_JAHR +1) *Starter.wbAnzahl);
		Datei.wbDateien (dateien, Verwendbare.DOWNLOADS_NACH +"/Europa", ".csv");
		System.out.println ("\n" +dateien.size() +" Saisondaten ausgewertet:");
		for (File datei : dateien) {
			Wettbewerb wettbewerb = Datei.ladeWb (datei.getPath());
			wettbewerb.durchlaufeSpiele (stats);
			stats.löscheSpiele();
		}
		Collections.sort (stats.kategorien, kategorie1_2ToreUndSieg);
		Ausgabe.instanz.text (stats.toString());
	}
	
}
