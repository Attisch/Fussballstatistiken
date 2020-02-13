package Speicherzugriff;

import java.util.ArrayList;

import Fussball.Chronologisch.Khw;
import Meldung.Fehler;
import Meldung.Formatfehler;
import Meldung.Wertangabefehler;

/**
 * Enthält Daten der gesuchten {@link Khw Kalenderhalbwoche}, die für die Ermittlung der aktuellen Khw bei der {@link Datei#ladeprozedere Ladeprozedere} verwendet wird.
 * Kleinere Khw als die gesuchte werden ignoriert. Falls es eine größere als die gesuchte Khw der aktuellen Khw zugeordnet wird, wird keine nächstaktuelle Khw festgelegt.
 * Andererseit falls eine Übereinstimmung gefunden wird, wird auch eine nächstaktuelle Khw festgelegt, falls sie existiert.
 * Zu jeder Khw werden ebenfalls die Pfäde der Dateien, die diese Khwn beinhalten, in einer Liste festgehalten. 
 * @author Attila Kiss
 */
public class KhwDateienSucher extends Khw implements Ladeprozedur {
	
	public String datei;
	private ArrayList<String> aktuelleDateien = new ArrayList<>(), nächstaktuelleDateien = new ArrayList<>();
	private WbDateiprüfer prüfer = new WbDateiprüfer(false);
	private Khw aktuelleKhw = new Khw(), nächstaktuelleKhw = new Khw();
	private boolean khwGelesen, letzteRunde;
	
	public KhwDateienSucher (String datei, Khw khw) throws Wertangabefehler {				
		super (khw.zahl, khw.monatszahl, khw.jahreszahl);
		this.datei = datei;
	}
	
	// Getter
	public String datei() {	return datei; }			// Implementierter Aufruf in der Ladeprozedur
	public Khw aktuelleKhw() { return aktuelleKhw; }
	public Khw nächstaktuelleKhw() { return nächstaktuelleKhw; }
	public ArrayList<String> aktuelleDateien() { return aktuelleDateien; }
	public ArrayList<String> nächstaktuelleDateien() { return nächstaktuelleDateien; }
	
	public void ende() throws Formatfehler {
		letzteRunde = khwGelesen = false;
		prüfer.ende();
	}
	
	public Boolean benutzt (String zeile) throws Fehler{
		switch (prüfer.benutzt(zeile)) {
			case KHW: khwGelesen = true; break;
			case SPIEL: 
				if (khwGelesen) {
					khwGelesen = false;
					Khw neueKhw = prüfer.neueKhw();
					int vergleich;				
					if (letzteRunde) {
						vergleich = nächstaktuelleKhw.compareTo(neueKhw);
						if (nächstaktuelleKhw.zahl==0 || vergleich >0)
							neueZuordnung (neueKhw, nächstaktuelleKhw, nächstaktuelleDateien);
						else if (vergleich==0) nächstaktuelleDateien.add(datei);
						return false;
					} else {
						vergleich = neueKhw.compareTo(this);
						if (vergleich >=0) {								// Wenn neue Khw größergleich gesuchte Khw
							if (vergleich==0) {								// Wenn gleich
								if (neueKhw.compareTo(aktuelleKhw) != 0) {
									if (aktuelleKhw.zahl !=0)
										verschiebeNachHinten();
									neueZuordnung (neueKhw, aktuelleKhw, aktuelleDateien);
								}
								else aktuelleDateien.add(datei);
								letzteRunde = true;
							} else {											// Wenn größer
								vergleich = neueKhw.compareTo(aktuelleKhw);	// Vergleiche neue Khw mit bisheriger näheste Khw
								if (aktuelleKhw.zahl==0 || vergleich< 0) {	// Wenn kleiner oder nicht belegt
									if (aktuelleKhw.zahl !=0)
										verschiebeNachHinten();
									neueZuordnung (neueKhw, aktuelleKhw, aktuelleDateien);
									return false;
								} else if (vergleich >0) {					// Wenn größer
									if (aktuelleKhw.compareTo(this)==0) {
										vergleich = nächstaktuelleKhw.compareTo(neueKhw);
										if (nächstaktuelleKhw.zahl==0 || vergleich >0)
											neueZuordnung (neueKhw, nächstaktuelleKhw, nächstaktuelleDateien);
										else if (vergleich==0)
											nächstaktuelleDateien.add(datei);
									}
									return false;
								} else {										// Wenn gleich
									aktuelleDateien.add(datei);
									if (aktuelleKhw.compareTo(this)==0) 
										letzteRunde = true;
								}
							}
						}
					}
				}
				break;
			case KEIN: return false;
			default:
		}
		return true;
	}
	
	/**
	 * Neue Khw wird der alten zugeordnet und alle Dateipfäde gelöscht und der neue Pfad hinzugefügt
	 * @param neueKhw
	 * @param alteKhw
	 * @param dateien
	 */
	private void neueZuordnung (Khw neueKhw, Khw alteKhw, ArrayList<String> dateien) {
		neueKhw.kopiereNach(alteKhw);
		dateien.clear();
		dateien.add(datei);
	}
	
	/**
	 * Verschiebt Inhalt von aktuellen Khw und Dateipfäden nach nächstaktuellen Khw und Dateipfäden
	 */
	private void verschiebeNachHinten() {
		aktuelleKhw.kopiereNach(nächstaktuelleKhw);
		nächstaktuelleDateien = aktuelleDateien;
		aktuelleDateien = new ArrayList<>();
	}

}
