package Speicherzugriff;

import Allgemein.Prozedur;
import Allgemein.Verwendbare;
import Fussball.Chronologisch.Khw;
import Meldung.Fehler;
import Meldung.Formatfehler;

/**
 * Ist eine {@link Prozedur}, die eine Wettbewerbdatei zeilenweise prüft, ob und welche Daten einer Liga sie beinhaltet und gibt anschließend stets einen {@link WbDateiorder} zurück.
 * @author Attila Kiss
 */
public class WbDateiprüfer implements Prozedur<WbDateiorder, String>{

	private String info;
	private Khw alteKhw = null, neueKhw = null;
	private String[] zeilenteile;
	private byte[] termindaten = new byte[6];
	private byte khwZahl;
	private boolean ersteZeile = true, khwGelesen, spielGelesen, infoFolgt, infoGelesen, vollständigkeitsprüfung;
	
	public WbDateiprüfer (boolean prüfung) {
		vollständigkeitsprüfung = prüfung;
	}
	
	String info() { return info; }							// Getter
	String[] zeilenteile() { return zeilenteile; }
	byte[] termindaten() { return termindaten; }
	Khw alteKhw() { return alteKhw; }
	Khw neueKhw() { return neueKhw; }

	public void ende() throws Formatfehler {
		if (vollständigkeitsprüfung) {
			String meldung = "Wettbewerb unvollständig: ";
			if (!khwGelesen)
				throw new Formatfehler (meldung +"keine Kalenderhalbwoche gefunden.");
			if (!spielGelesen)
				throw new Formatfehler (meldung +"kein Spiel gefunden oder die letze Kalenderhalbwoche hat keine Spiele angegeben.");
			if (!infoFolgt || !infoGelesen)
				throw new Formatfehler (meldung +"Info fehlt oder die leere Zeile davor.");	
		}
		ersteZeile = true;
		infoFolgt = khwGelesen = spielGelesen = infoGelesen = false;
		neueKhw.zurücksetzen();
	}
	
	public WbDateiorder benutzt (String zeile) throws Fehler {
		if (ersteZeile) {																	// Erste Zeile
			if (zeile.startsWith("Datum"))
				ersteZeile = false;
			else throw new Formatfehler ("Unbekannter Inhalt in der ersten Zeile.");
			return WbDateiorder.ERSTE_ZEILE;
		} else if (zeile.endsWith(".KHW")) {												// Khw Zeile
			if (neueKhw != null) {
				if (alteKhw==null)
					alteKhw = neueKhw.clone();
				else neueKhw.kopiereNach(alteKhw);
			}
			try {
				khwZahl = Byte.parseByte (zeile.split("[.]",2)[0]);
			} catch (NumberFormatException e) {
				throw new Formatfehler ("Ungültige Kalenderhalbwochenangabe in der Zeile: " +zeile);
			}
			khwGelesen = true;
			spielGelesen = false;
			return WbDateiorder.KHW;
		} else if (khwGelesen) {															// Spiel Zeile
			if ((zeilenteile = Datei.spiel (zeile, Verwendbare.SPALTENANZAHL)) !=null) {
				Datei.terminteile (zeilenteile, termindaten);
				if (!spielGelesen) {														// Alte und neu eingelesene Khw vergleichen
					neueKhw = new Khw (khwZahl, termindaten[1], termindaten[2]);
					if (alteKhw != null && alteKhw.compareTo(neueKhw) >0)					// Prüfung, ob die Kalenderhalbwochen chronologisch richtig geordnet sind
						throw new Formatfehler ("Alte Kalenderhalbwoche ist später als die neue Kalenderhalbwoche. Alte: " +alteKhw.toString() +". Neue: " +neueKhw.toString());
				}
				spielGelesen = true;
				return WbDateiorder.SPIEL;
			} else if (spielGelesen) {
				if (zeile.isEmpty() && !infoFolgt)
					infoFolgt = true;														// Ligadateiorder.KEIN
				else if (infoFolgt && zeile.startsWith("Info:;")) {
					info = zeile.substring(6);
					Datei.infoprüfung (info);
					info = info.replaceAll("/", " → ");
					infoGelesen = true;
					return WbDateiorder.INFO;
				}
				else throw new Formatfehler ("Unbekannter Inhalt.");
			}
		} else throw new Formatfehler ("Unbekannter Inhalt in der zweiten Zeile.");
		return WbDateiorder.KEIN;
	}
}
