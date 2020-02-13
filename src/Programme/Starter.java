package Programme;

import java.io.File;
import java.util.ArrayList;

import Allgemein.Verwendbare;
import Visualität.Ausgabe;
import Fussball.Wettbewerb;
import Fussball.Chronologisch.KhwSpiele;
import Fussball.Chronologisch.KhwSucher;
import Fussball.Chronologisch.Zeitpunkt;
import Fussball.Statistiken.Ausgewertete;
import Meldung.Fehler;
import Meldung.Wertangabefehler;
import Speicherzugriff.Datei;
import Speicherzugriff.KhwDateien;
import Speicherzugriff.Ordner;

/**
 * Startet den Fussballtipphelfer
 * @author Attila Kiss
 */
public final class Starter {
	
	public static final short wbAnzahl = wbAnzahl(); 
	
	private Starter() {}
	
	public static void main(String[] args) {
		if (wbAnzahl< 0) {
			Ausgabe.instanz.text ("Keine Ligen zum Einlesen verfügbar.");
		} else try {
			Ausgabe.instanz.text ("Anzahl der verfügbaren Ligen: " +wbAnzahl);
			startmenü();
		} catch (Exception e) {
			Ausgabe.instanz.text (Fehler.entstehung(e));
		}
		Ausgabe.instanz.text ("\nProgrammende");
	}
	
	/**
	 * @return Anzahl der verfügbaren Wettbewerbe
	 */
	private static short wbAnzahl() {
		short anzahl = 0;
		ArrayList<Ordner> kontinente;
		try {
			kontinente = Datei.kontinente_länder_ligen();
			for (Ordner kontinent : kontinente)
				for (Ordner land : kontinent.unterordnerliste)
					anzahl += land.unterordnerliste.size();
		} catch (Wertangabefehler e) {
			Ausgabe.instanz.text (Fehler.entstehung(e));
		}
		return anzahl;
	}

	private static void startmenü() throws Fehler {
		byte menüNr;	
		do {
			menüNr = Ausgabe.menü ("MENÜ", "Beende das Programm", "Saisonansicht der Wettbewerbe", "Aktuelle Spiele", "Statistik der 2.Halbzeit in Bezug auf das Ergebnis der 1.Halbzeit");
			switch (menüNr) {
				case 1: wbMenü(); break;
				case 2: aktuelleKhwMenü(); break;
				case 3: Ausgewertete.hzEndstandStats(); Ausgabe.weiterMitEnter(); break;
				default:
			}
		} while (menüNr >0);
	}
	
	/**
	 * Menü mit der Auswahl der aktuellen {@link Khw Kalenderhalbwochen}
	 * @throws Fehler
	 */
	private static void aktuelleKhwMenü() throws Fehler {
		byte menüNr;
		KhwDateien[] dateienlisten = Datei.aktuelleKhwDateien();
		int länge = dateienlisten.length;
		String[] menütext = new String[länge];
		for (byte b = 0; b< länge; b++) {
			menütext[b] = "Spiele in der " +dateienlisten[b].khw;
			if (länge==2 && b==0)
				menütext[b] += " (aktuelle Kalenderhalbwoche)";
			else menütext[b] += " (kommende Kalenderhalbwoche)";
		}
		menüNr = Ausgabe.menü ("Kalenderhalbwochen", "Zurück zum Hauptmenü", menütext);
		if (menüNr==1)
			khwWbAuswahlMenü (dateienlisten[0], true);
		else if (länge >1 && menüNr==2)
			khwWbAuswahlMenü (dateienlisten[1], false);
	}
	
	/**
	 * Menüansicht einer Kalenderhalbwoche (Khw) mit deren {@link Wettbewerb Wettbewerben}
	 * @param khwDateien sind die Dateien, die alle eine bestimmte Kalenderhalbwoche besitzen
	 * @param aktuelleKhw gibt an, ob die aktuelle Khw angezeigt wird oder nicht (also kommende Khw)
	 * @throws Fehler 
	 */
	private static void khwWbAuswahlMenü (KhwDateien khwDateien, boolean aktuelleKhw) throws Fehler {
		byte menüNr;
		String khwText = "folgender";
		String[] menütext = new String[khwDateien.dateien.length];
		if (aktuelleKhw)
			khwText = "aktueller";
		for (byte b = 0; b< khwDateien.dateien.length; b++)
			menütext[b] = Datei.infoOhneJahr(khwDateien.dateien[b].toString());
		menüNr = Ausgabe.menü ("Ligen mit Spielen in " +khwText +" Kalenderhalbwoche: " +Zeitpunkt.aktuell.khw, "Zurück zu den Kalenderhalbwochen", menütext);
		if (menüNr >0)
			khwSpieleMenü (khwDateien.dateien[menüNr-1], khwDateien, aktuelleKhw);
		else aktuelleKhwMenü();
	}
	
	/**
	 * Menü der Spiele einer Kalenderhalbwoche eines {@link Wettbewerb Wettbewerbs}
	 * @param datei des aktuellen wettbewerbs
	 * @param khwDateien ist die Auswahl der Wettbewerbe
	 * @param aktuelleKhw gibt an, ob die aktuelle Khw angezeigt wird oder nicht (also kommende Khw)
	 * @throws Fehler
	 */
	private static void khwSpieleMenü (String datei, KhwDateien khwDateien, boolean aktuelleKhw) throws Fehler {
		Wettbewerb wb = Datei.ladeWb(datei);
		KhwSpiele khwSpiele = wb.khwSpiele (new KhwSucher(khwDateien.khw));
		Ausgabe.instanz.text("\n" +wb.name() +khwSpiele.toString());
		Ausgabe.weiterMitEnter();
		khwWbAuswahlMenü (khwDateien, aktuelleKhw);
	}
	
	/**
	 * Auswahl aller {@link Wettbewerb}e
	 * @throws Fehler
	 */
	private static void wbMenü() throws Fehler {
		byte menüNr;
		String[] menütext = Datei.ligennamen();
		menüNr = Ausgabe.menü ("Wettbewerbe", "Zurück zum Hauptmenü", menütext);
		if (menüNr >0)
			wbSaisonsMenü (menütext[menüNr-1]);
	}
	
	/**
	 * Auswahl der Saisons eines {@link Wettbewerb Wettbewerbs}
	 * @param infoOhneJahr
	 * @throws Fehler
	 */
	private static void wbSaisonsMenü (String infoOhneJahr) throws Fehler {
		byte menüNr;
		infoOhneJahr = infoOhneJahr.replaceAll(" → ", "/");
		ArrayList<File> dateien = new ArrayList<>();
		Datei.wbDateien (dateien, Verwendbare.DOWNLOADS_NACH +"/" +infoOhneJahr, ".csv");
		String[] menütext = new String[dateien.size()];
		String path;
		for (byte b = 0; b< dateien.size(); b++) {
			path = dateien.get(b).getPath();
			menütext[b] = "Saisonanfang" +path.substring (path.length()-9, path.length()-4);
		}
		menüNr = Ausgabe.menü (infoOhneJahr.split("/", 3)[2], "zurück zur Wettbewerbauswahl", menütext);
		if (menüNr >0)
			saisonMenü (dateien.get(menüNr-1).getPath());
		else wbMenü();
	}
	
	/**
	 * Abbildung einer Saison eines {@link Wettbewerb Wettbewerbs}
	 * @param datei
	 * @throws Fehler
	 */
	private static void saisonMenü (String datei) throws Fehler {
		Wettbewerb wb = Datei.ladeWb(datei);
		Ausgabe.instanz.text(wb.toString());
		Ausgabe.weiterMitEnter();
		wbSaisonsMenü (Datei.infoOhneJahr(datei));
	}

	
}