package Fussball.Statistiken;

import java.util.ArrayList;

import Fussball.KhwProzedur;
import Fussball.Spielprozedurorder;
import Fussball.Chronologisch.KhwSpiele;
import Fussball.Spielobjekte.ReguläresSpiel;
import Fussball.Spielobjekte.Spiel;

/**
 * Enthält eine allgemeine Statistik der 2. Halbzeit unter Berücksichtigung der 1. Hz was Sieg, Remis oder Niederlage betrifft und eine allgemeine Statistik extra zu jedem Ergebnis.
 * Es besteht optional die Möglichkeit die Spielelisten zwischenzeitlich zu löschen, ohne die Statistik dabei zu beeinflussen. Bei größerer Datenmenge ist dies unumgänglich.
 * @author Attila Kiss
 */
public class HzEndergebnisstatistiker extends Stats implements KhwProzedur {

	Stats hzSieg = new Stats(), hzRemis = new Stats(), hzNiederlage = new Stats();
	ArrayList<StatsNachHzErgebnis> kategorien = new ArrayList<>();
	
	public HzEndergebnisstatistiker () {}

	public Spielprozedurorder benutzt (KhwSpiele khwSpiele) {
		boolean aufgenommen = false;
		for (Spiel spiel : khwSpiele.spiele) {
			if (spiel.gesetztesSpiel())
				return Spielprozedurorder.ENDE;
			if (spiel instanceof ReguläresSpiel && !spiel.spielÜ90()) {
			
				// S-R-N Einteilung
				ReguläresSpiel rs = (ReguläresSpiel) spiel;
				byte heimDiff = (byte) (rs.heimtore -rs.heimtoreHz);
				byte auswDiff = (byte) (rs.auswärtstore -rs.auswärtstoreHz);
				if (rs.heimtoreHz==rs.auswärtstoreHz)
					hzRemis.ergänze(heimDiff, auswDiff);
				else if (rs.heimtoreHz >rs.auswärtstoreHz)
					hzSieg.ergänze(heimDiff, auswDiff);
				else hzNiederlage.ergänze(heimDiff, auswDiff);
				
				// Ergebniseinteilung
				for (StatsNachErgebnis<ReguläresSpiel> kategorie : kategorien) {
					if (kategorie.heimtore==rs.heimtoreHz && kategorie.auswärtstore==rs.auswärtstoreHz) {
						kategorie.ergänze (heimDiff, auswDiff);
						kategorie.spieleliste.add(rs);
						aufgenommen = true;
						break;
					}
				}
				if (!aufgenommen) {
					StatsNachHzErgebnis neueKategorie = new StatsNachHzErgebnis (rs.heimtoreHz, rs.auswärtstoreHz);
					neueKategorie.ergänze (heimDiff, auswDiff);
					neueKategorie.spieleliste.add(rs);
					kategorien.add (neueKategorie);
				}
			}
		}
		return Spielprozedurorder.NÄCHSTE_KHW;
	}
	/**
	 * Löscht die Spielelisten der Kategorien, d.h. dass die Teamnamen usw. entfernt werden aber die Statistiken erhalten bleiben
	 */
	public void löscheSpiele() {
		for (StatsNachErgebnis<ReguläresSpiel> kategorie : kategorien)
			kategorie.spieleliste.clear();
	}
	
	public void ende() {}
	
	public String toString() {
		String text = "\n2.Halbzeitstatistik beim Heimsieg zur 1.Halbzeit: " +hzSieg.toString() 
						+"\n2.Halbzeitstatistik beim Remis zur 1.Halbzeit: " +hzRemis.toString() 
						+"\n2.Halbzeitstatistik beim Auswärtssieg zur 1.Halbzeit: " +hzNiederlage.toString()
						+"In " +kategorien.size() +" Kategorien aufgeteilt:\n";
		for (StatsNachHzErgebnis kategorie : kategorien)
			text += kategorie.toString();
		return text;
	}
}
