package Fussball;

import Baumwurzelstruktur.Wurzelstruktur;
import Fussball.Chronologisch.*;
import Meldung.Fehler;
import Meldung.Wertangabefehler;

/**
 * Beinhaltet allgemein gültigen Informationen zum Wettbewerb, die Liste der Teamnamen, die im Wettbewerb teilnehmen und die Liste der {@link Jahr Jahre} in denen die Spiele stattfinden.
 * Außerdem wird stets die aktuellen Daten festgehalten, die von Interesse sind: {@link #khwSpiele Spiele}, {@link #khw Kalenderhalbwoche} und {@link #index Index}.
 * @author Attila Kiss
 */
public class Wettbewerb {
	
	/** Inhalt: "KONTINENT → LAND(/VEREIN) → LIGA JAHRESZAHL" */
	public final String info;
	public final Wurzelstruktur<String> teamnamen;
	public final Jahr[] jahre;
	public final byte längsterName;
	
	/**
	 * Erzeugt einen Wettbewerb
	 * @param info ist der Dateipfad mit Dateinamen und ohne Dateisuffix
	 * @param teamnamen 
	 * @param khwn sind die Kalenderhalbwochen in denen die Spiele des Wettbewerbs stattfinden
	 * @throws Wertangabefehler 
	 */
	public Wettbewerb (String info, Wurzelstruktur<String> teamnamen, Jahr[] jahre, byte längsterName) throws Wertangabefehler {
		this.info = info;
		this.teamnamen = teamnamen;
		this.jahre = jahre;
		this.längsterName = längsterName;
	}
	
	/** Gibt den Namen des Wettbewerbs wieder */
	public String name() {
		return info.split(" → ", 3)[2];
	}
	

	public void durchlaufeSpiele (KhwProzedur prozedur) throws Fehler {
		Spielprozedurorder order = Spielprozedurorder.KEIN;
		KhwSpiele khwSpiele = null;
		byte khwIndex;
		int länge;
		try {
			for (Jahr jahr : jahre) {
				for (Monat monat : jahr.monate) {
					länge = monat.khwSpiele.length;
					khwIndex = 0;
					do {
						khwSpiele = monat.khwSpiele[khwIndex++];
						order = prozedur.benutzt(khwSpiele);
					} while (khwIndex< länge && order.compareTo(Spielprozedurorder.NÄCHSTE_KHW) <=0);
					if (order.compareTo(Spielprozedurorder.NÄCHSTES_JAHR) >=0)
						break;
				}
				if (order.compareTo(Spielprozedurorder.ENDE)==0)
					break;
			}
			prozedur.ende();
		} catch (Exception e) {
			String meldung = "Fehler beim Durchlaufen der Spiele des Wettbewerbs '" +info +"' aufgetreten. ";
			if (khwSpiele != null)
				meldung += "Aktuelle Kalenderhalbwoche: " +khwSpiele.toString() +"\n";
			prozedur.ende();
			throw new Fehler (meldung +Fehler.pfadteilangabe(e, "Fussball.Wettbewerb.durchlaufeSpiele"));
		}
	}
	
	/**
	 * @return die Spiele der {@link KhwSucher Kalenderhalbwochensuchers}
	 * @param khwSucher
	 * @throws Fehler 
	 */
	public KhwSpiele khwSpiele (KhwSucher khwSucher) throws Fehler {
		durchlaufeSpiele (khwSucher);
		return khwSucher.gefundenes();
	}
	
	/**
	 * @return die Spiele einer Kalenderhalbwoche, die unter dem Index zu finden ist
	 * @param index {@link KhwIndex}
	 */
	public KhwSpiele khwSpiele (KhwIndex index) {
		return jahre[index.jahr].monate[index.monat].khwSpiele[index.khw];
	}
	
	
	public String toString() {
		String text = "\n" +info.toString()+" mit "+teamnamen.anzahl()+" Teilnehmer: \n"+teamnamen.toString()+"\n";
		for (Jahr jahr : jahre)
			text += jahr.toString();
		return text;
	}
 }
