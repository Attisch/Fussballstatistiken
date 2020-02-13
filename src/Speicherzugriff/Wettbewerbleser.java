package Speicherzugriff;

import java.util.ArrayList;

import Baumwurzelstruktur.Wurzelstruktur;
import Fussball.Wettbewerb;
import Fussball.Chronologisch.Jahr;
import Fussball.Chronologisch.KhwSpiele;
import Fussball.Chronologisch.Monat;
import Fussball.Chronologisch.Zeitpunkt;
import Fussball.Chronologisch.Khw;
import Fussball.Spielobjekte.GesetztesSpiel;
import Fussball.Spielobjekte.Spiel;
import Meldung.Fehler;
import Meldung.Formatfehler;

/**
 * Kreiert aus Zeilen einer Datei mit Daten eines Wettbewerbs einen {@link Wettbewerb}.
 * @author Attila Kiss
 */
public class Wettbewerbleser implements Ladeprozedur {

	public String datei;
	private Wettbewerb wettbewerb;
	private WbDateiprüfer prüfer = new WbDateiprüfer(true);
	private ArrayList<Jahr> jahre = new ArrayList<>();
	private ArrayList<Monat> monate = new ArrayList<>();
	private ArrayList<KhwSpiele> khwn = new ArrayList<>();
	private ArrayList<Spiel> spiele = new ArrayList<>();
	private Wurzelstruktur<String> teamnamen = new Wurzelstruktur<String>();
	private byte längsterName;
	private boolean prüfeListen;
	
	public Wettbewerbleser (String datei) throws Formatfehler {
		this.datei = datei;
	}

	// Getter
	public String datei() { return datei; }		// Implementierter Aufruf in der Ladeprozedur
	public Wettbewerb wettbewerb() { return wettbewerb; }
	
	public void ende() throws Formatfehler {
		prüfer.ende();
		jahre.clear();
		teamnamen = new Wurzelstruktur<String>();
		längsterName = 0;
		prüfeListen = false;
	}
	
	public Boolean benutzt (String zeile) throws Fehler {
		WbDateiorder order = prüfer.benutzt(zeile);
		Khw alteKhw = prüfer.alteKhw(), neueKhw = prüfer.neueKhw();
		switch (order) {
		case KHW:
			if (spiele.size() >0) {				// Falls beim ersten Einlesen der Khw noch keine Spiele gibt
				spieleEinordnen (alteKhw);
				prüfeListen = true;
			}
			break;
		case SPIEL:								// In die Listen einordnen
			if (prüfeListen && alteKhw.zahl != neueKhw.zahl && alteKhw.monatszahl !=neueKhw.monatszahl) {
				monateEinordnen (neueKhw.monatszahl);									// Neuer Monat
				if (alteKhw.jahreszahl !=neueKhw.jahreszahl)							// Neues Jahr
					jahreEinordnen (alteKhw.jahreszahl);
			}
			prüfeListen = false;
			
			// Namenaufnahme
			String[] zeilenteile = prüfer.zeilenteile();
			for (byte b = 3; b< 5; b++) {
				String teamname = teamnamen.gefunden(zeilenteile[b]);
				if (teamname==null) {
					teamnamen.verwurzel(zeilenteile[b]);
					if (zeilenteile[b].length() >längsterName)
						längsterName = (byte) zeilenteile[b].length();
				} 
				else zeilenteile[b] = teamname;
			}
			
			// Spielaufnahme
			Spiel spiel = Spielwahl.lade (prüfer.termindaten(), zeilenteile);
			spiele.add (spiel);
			spiel.kalendertagprüfung (neueKhw.monatszahl, neueKhw.jahreszahl);
			break;
		case INFO:
			spieleEinordnen (neueKhw);
			monateEinordnen (neueKhw.monatszahl);
			jahreEinordnen (neueKhw.jahreszahl);
			wettbewerb = new Wettbewerb (prüfer.info(), teamnamen, jahre.toArray (new Jahr[jahre.size()]), längsterName);				// Neuer Wettbewerb 
			return false;
		default:
		}
		return true;
	}

	private void spieleEinordnen (Khw khw) {
		khwn.add (new KhwSpiele (khw.zahl, khw.monatszahl, Zeitpunkt.zweistelligesJahr(khw.jahreszahl), spiele.toArray (new GesetztesSpiel[spiele.size()])));
		spiele.clear();
	}

	private void monateEinordnen (byte monat) {
		monate.add (new Monat (monat, khwn.toArray (new KhwSpiele[khwn.size()])));
		khwn.clear();
	}
	
	private void jahreEinordnen (short jahr) {
		jahre.add (new Jahr (Zeitpunkt.zweistelligesJahr(jahr), monate.toArray (new Monat[monate.size()])));
		monate.clear();
	}
}
