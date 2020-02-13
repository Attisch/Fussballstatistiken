package Fussball.Chronologisch;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import Allgemein.Verwendbare;
import Fussball.Chronologisch.Wochentag;
import Meldung.Dateilesefehler;
import Meldung.Dateischreibfehler;
import Meldung.Fehler;
import Meldung.Wertangabefehler;
import Speicherzugriff.Datei;
import Speicherzugriff.Startdatumleser;

/**
 * Enthält einen Zeitpunkt, sowie den Zugriff auf den aktuellen Zeitpunkt, der bei Fehler null sein kann. Automatisch wird die Jahreszahl {@link #jahreszahlprüfung(short) geprüft} und
 * die Berechnung der Kalenderhalbwochenzahl und des Wochentags zu gewährleistet, falls nicht bekannt.
 * @author Attila Kiss
 */
public class Zeitpunkt extends Tageszeit<Zeitpunkt> {

	public Khw khw;
	public static Zeitpunkt aktuell = aktuelleInit();
	
	private Zeitpunkt() {
		khw = new Khw();
	}
	
	/**
	 * Die Kalenderhalbwochenzahl und Wochentag wird automatisch errechnet, nachdem die Werte geprüft worden sind.
	 * @throws Wertangabefehler
	 */
	public Zeitpunkt (byte kalendertag, byte monat, short jahr) throws Wertangabefehler {
		super (kalendertag, (byte) Wochentag.UNBEKANNT.ordinal());
		khw = new Khw (monat, jahr);
		this.berechneKhw (aktuell);
	}
	
	/**
	 * Zeitpunkt ohne Uhrzeit, aber mit Kalenderhalbwoche (Khw) (wie z.B. auch das Startdatum). Alle Parameter werden geprüft.
	 * @param terminteile besteht aus 4 Teilen
	 * @param khwZahl
	 * @throws Wertangabefehler 
	 */
	private Zeitpunkt (byte[] terminteile, byte khwZahl) throws Wertangabefehler {
		khw = new Khw();
		geprüfteZuweisung (terminteile[0], terminteile[1], terminteile[2], terminteile[3], khwZahl);
	}
	
	/**
	 * Ungeprüfte Übergabe der Parameter.
	 * @throws Wertangabefehler
	 */
	private Zeitpunkt (byte kalendertag, byte monat, short jahr, byte wochentag, byte stunde, byte minute, byte khwZahl) throws Wertangabefehler {
		khw = new Khw();
		zuweisen (kalendertag, monat, jahr, wochentag, stunde, minute, khwZahl);
	}
	
	/**
	 * Datum wird nach erfolgreicher Prüfung zugewiesen.
	 * @param kalendertag
	 * @param monat
	 * @param jahr
	 * @throws Wertangabefehler
	 */
	public void datumzuweisung (byte kalendertag, byte monat, short jahr) throws Wertangabefehler {
		this.kalendertag = kalendertag;
		khw.monat(monat);
		khw.jahr(jahr);
		kalendertagprüfung(monat, jahr);
	}

	/**
	 * Alle Werte geprüft zuweisen. Uhrzeit wird nicht erfasst.
	 * @param kalendertag
	 * @param monat
	 * @param jahr
	 * @param wochentag
	 * @param khwZahl
	 * @throws Wertangabefehler
	 */
	public void geprüfteZuweisung (byte kalendertag, byte monat, short jahr, byte wochentag, byte khwZahl) throws Wertangabefehler {
		datumzuweisung (kalendertag, monat, jahr);
		this.wochentag (wochentag);
		khw.zahl (khwZahl);
	}
	
	/**
	 * Alle Werte einfach ungeprüft zuweisen
	 * @param kalendertag
	 * @param monat
	 * @param jahr
	 * @param wochentag
	 * @param stunde
	 * @param minute
	 * @param khwZahl
	 * @throws Wertangabefehler 
	 */
	private void zuweisen (byte kalendertag, byte monat, short jahr, byte wochentag, byte stunde, byte minute, byte khwZahl) throws Wertangabefehler {
		this.kalendertag = kalendertag;
		this.wochentag = wochentag;
		khw.zuweisen (khwZahl, monat, jahr);
		this.stunde = stunde;
		this.minute = minute;
	}

	/**
	 * Legt den aktuellen Zeitpunkt zum erstem mal fest.
	 * @returns Zeitpunkt oder null
	 */
	public static Zeitpunkt aktuelleInit() {
		ZonedDateTime now = ZonedDateTime.now();
		Zeitpunkt aktuell = null;
		try {
			aktuell = new Zeitpunkt ((byte) now.getDayOfMonth(), (byte) now.getMonthValue(), (short) now.getYear(),
									(byte) (now.getDayOfWeek().ordinal() +1), (byte) now.getHour(), (byte) now.getMinute(), (byte) 0);
			Zeitpunkt.aktuell = aktuell;
			aktuell.berechneKhw (geladen());
		} catch (Wertangabefehler | Dateilesefehler e) {
			Visualität.Ausgabe.instanz.text ("Fehler beim Laden des aktuellen Zeitpunktes aufgetreten." +Fehler.entstehung(e));
		}
		return aktuell;
	}

	/**
	 * Ändert die Kalenderhalbwoche des aktuellen Termins auf die nächstfolgende oder vorherige Khw.
	 * @param richtung gleich true, wenn die Kalenderhalbwoche auf einen Tag später gesetzt werden soll, ansonten false für früher	
	 */
	void ändereKHW (int richtung) {
		khw.zahl += richtung;
		if (richtung==1) {
			if (((khw.zahl==104 || khw.zahl==105) && khw.monatszahl==1) || khw.zahl==106)
				khw.zahl = 1;
		} else if (khw.zahl==0) {
			int resttage;
			byte khwTage;
			if (khw.monatszahl==1)
				resttage = restJahresTage(khw.jahreszahl -1) +kalendertag;	
			else resttage = restJahresTage(khw.jahreszahl) -(31 -kalendertag);
			if (kalendertag==0)
				khwTage = 4;
			else khwTage = 3;
			if (resttage >3 || (resttage==3 && khwTage==3))
				khw.zahl = 105;
			else khw.zahl = 104;
		}
	}

	/**
	 * Ändert das aktuelle Datum auf den nächstfolgenden oder vorherigen Monat. Ändert auch das Jahr, falls erforderlich.
	 * @param richtung gleich 1, wenn das Datum auf einen Monat später gesetzt werden soll, ansonten -1 für einen Monat früher
	 */
	void ändereMonat (int richtung) {
		khw.monatszahl += richtung;
		if (richtung==1) {
			if (khw.monatszahl==13) {
				khw.monatszahl = 1;
				khw.jahreszahl++;
			}
		} else if (khw.monatszahl==0) {
			khw.monatszahl = 12;
			khw.jahreszahl--;
		}
	}

	/**
	 * Ändert das aktuelle Datum auf den nächstfolgenden oder vorherigen Tag. Ändert auch alle anderen Variablen des Zeitpunktes, falls erforderlich.
	 * @param richtung gleich 1, wenn das Datum auf einen Tag später gesetzt werden soll, ansonten -1 für einen Tag früher
	 */
	void ändereTag (int richtung) {
		kalendertag += richtung;
		if (richtung==1) {
			if (kalendertag==maxMonatstage(khw.monatszahl , khw.jahreszahl)+1) {
				kalendertag = 1;
				ändereMonat (richtung);
			}
		} else if (kalendertag==0) {
			ändereMonat (richtung);
			kalendertag = maxMonatstage(khw.monatszahl, khw.jahreszahl);
		}
		ändereWochentag (richtung);
//		System.out.println("Neuer Tag: " +toString());
	}

	/**
	 * Ändert den {@link Wochentag} des aktuellen Datums auf den nächstfolgenden oder vorherigen Wochentag.
	 * Ändert die Kalenderhalbwoche ebenfalls, falls notwendig.
	 * @param richtung gleich 1, wenn der Wochentag auf einen Tag später gesetzt werden soll, ansonten -1 für einen Wochentag früher
	 */
	void ändereWochentag (int richtung) {
		wochentag += richtung;
		if (richtung==1) {							// Vorwärts
			if (wochentag==8)
				wochentag = 1;
			if (wochentag==2 || wochentag==5)
				ändereKHW (richtung);
		} else {									// Rückwärts
			if (wochentag==0)
				wochentag = 7;
			if (wochentag==1 || wochentag==4)
				ändereKHW (richtung);
		}
	}

	/**
	 * Berechnet die Kalenderhalbwoche (Khw) des aktuellen Zeitpunktes, indem das Datum des anderen Zeitpunktes mit Khw solange an das Datum des einen sich annähert,
	 * bis sie gleich sind, um die errechnete Khw abschließend zuzuweisen. Der Wochentag der Khw wird ebenfalls verglichen oder beim Nichtvorhandensein übernommen und abschließend geprüft.
	 * @param ander ist der sich annähernde Zeitpunkt, der bis auf die Uhrzeit vollständig sein muss
	 * @throws Wertangabefehler 
	 */
	public void berechneKhw (Zeitpunkt ander) throws Wertangabefehler {
		if (!gleicherTag (ander)) {
			int richtung = richtung (compareTo (ander));
//				System.out.println("Aktuell Startwert: " +toString());
//				System.out.println("Zuletzt Startwert: " +ander.toString());
			while (!gleicherTag(ander) && richtung==richtung (compareTo (ander)))
				if (!ander.wochensprung (richtung)) 
					ander.ändereTag (richtung);
			if (richtung != richtung (compareTo (ander))) 
				richtung *= -1;
			while (!gleicherTag(ander))
				ander.ändereTag (richtung);
		}
		if (wochentag >0) {
			if (wochentag != ander.wochentag)
				throw new Wertangabefehler ("Fehler bei der Berechnung der Kalenderhalbwoche: Gegebener Wochentag "
											+Wochentag.values()[wochentag] +" und errechneter Wochentag " +Wochentag.values()[ander.wochentag]);
		} else wochentag = ander.wochentag;
		khw.zahl = ander.khw.zahl;
		kalendertagprüfung (khw.monatszahl, khw.jahreszahl);
//			System.out.println("Aktuell: " +toString());
//			System.out.println("Zuletzt: " +ander.toString());
	}

	/**
	 * Anzahl der Tage, die abzüglich der 52 Wochen im Jahr aufgrund der Überprüfung eines Schaltjahres übrig bleiben
	 * @param jahr
	 * @return 1 oder 2
	 */
	byte restJahresTage (int jahr) {
		if (jahr%4==0)
			return 2;
		return 1;
	}

	/**
	 * Gibt eine Richtung aufgrund eines Wertes vor: +1, wenn der Wert größer 0 ist; -1, wenn der Wert kleiner 0 ist, ansonsten 0.
	 * @param wert ist eine Zahl
	 * @return 0, 1, oder -1
	 */
	int richtung (int wert) {
		if (wert >0) return --wert;
		else if (wert< 0) return ++wert;
		return 0;
	}

	/**
	 * Springt das aktuelle Datum auf den nächstfolgenden oder vorherigen Woche. Aktualisiert alle Variablen des Zeitpunktes dabei.
	 * @param richtung gleich 1, wenn das Datum auf eine Woche später gesetzt werden soll, ansonten -1 für eine Woche früher
	 * @return true, wenn ausgeführt, ansonsten false und Datum bleibt unverändert
	 */
	boolean wochensprung (int richtung) {
		if ((richtung==1 && kalendertag< maxMonatstage(khw.monatszahl, khw.jahreszahl)-6 && khw.zahl< 103) || (richtung==-1 && kalendertag >7 && khw.zahl >2)) {
			kalendertag += richtung *7;
			khw.zahl += richtung *2;
//				System.out.println("Wochenvorsprung nach: " +toString());
			return true;
		}
		return false;
	}

	/**
	 * Überprüft, ob zwei Zeitpunkte am gleichen Tag sind oder nicht
	 * @param ander ist der andere Zeitpunkt, womit der eine verglichen wird
	 * @return true, wenn am gleichen Tag
	 */
	boolean gleicherTag (Zeitpunkt ander) {
		if (khw.jahreszahl==ander.khw.jahreszahl && khw.monatszahl==ander.khw.monatszahl && kalendertag==ander.kalendertag)
			return true;
		return false;
	}

	public void kopiereNach (Zeitpunkt nach) throws Wertangabefehler {
		nach.zuweisen (kalendertag, khw.monatszahl, khw.jahreszahl, wochentag, stunde, minute, khw.zahl);
	}
	
	public Zeitpunkt clone() throws CloneNotSupportedException {
		Zeitpunkt zeitpunkt = new Zeitpunkt();
		try {
			kopiereNach (zeitpunkt);
		} catch (Wertangabefehler e) {
			throw new CloneNotSupportedException();
		}
		return zeitpunkt;
	}

	/**
	 * @return -2 oder 2 bei keiner Datumübereinstimmung, ansonsten -1 oder 1 bei keiner Tageszeitübereinstimmung
	 */
	public int compareTo (Zeitpunkt ander) {
		return gleicherTag(ander) ? super.compareTo(ander) : this.khw.jahreszahl< ander.khw.jahreszahl || (this.khw.jahreszahl==ander.khw.jahreszahl && 
			(this.khw.monatszahl< ander.khw.monatszahl || (this.khw.monatszahl==ander.khw.monatszahl && this.kalendertag< ander.kalendertag))) ?  -2 : 2;
	}
	
	public String toString() {
		return khw.zahl +".Khw " +kalendertag +"." +khw.monatszahl +"." +khw.jahreszahl +" " +super.toString();
	}

	static Zeitpunkt geladen() throws Wertangabefehler, Dateilesefehler {
		Startdatumleser sdLeser = new Startdatumleser();
		Datei.ladeprozedere(sdLeser);
		return new Zeitpunkt (sdLeser.terminteile, sdLeser.khwNr);
	}

	/**
	 * @return die maximale Anzahl der Tage im gegebenen Monat
	 */
	static byte maxMonatstage (byte monat, short jahr) {
		if (monat==2)
			if (jahr%4==0)
				return 29;
			else return 28;
		else if (monat==4 || monat==6 || monat==9 || monat==11)
			return 30;
		else return 31;
	}

	/**
	 * Speichert den unter 'aktuell' befindlichen Termin als CSV-Datei
	 * @throws Dateischreibfehler
	 */
	public static void speichern() throws Dateischreibfehler {
		ArrayList<String> text = new ArrayList<>(6);
		text.add ("Name;Wert");
		text.add ("Tag;" +aktuell.kalendertag);
		text.add ("Monat;" +aktuell.khw.monatszahl);
		text.add ("Jahr;" +aktuell.khw.jahreszahl);
		text.add ("Wochentag;" +Wochentag.values()[aktuell.wochentag].abkürzung());
		text.add ("KHW;" +aktuell.khw.zahl);
		Datei.speicher (text, "Zeit/Startdatum.csv");
	}

	/**
	 * @return Vierstellige Jahreszahl. Wenn die Jahreszahl kleiner hundert ist, wird anhand des heutigen Datums angepasst.
	 * @param jahreszahl
	 * @throws Wertangabefehler
	 * @see {@link #vierstelligesJahr(int)}
	 */
	public static short jahreszahlprüfung (short jahreszahl) throws Wertangabefehler {
		if (jahreszahl< 100)
			return vierstelligesJahr(jahreszahl);
		return jahreszahl;
	}
	
	/**
	 * Wandelt ein zweistelliges Jahr in ein vierstelliges Jahr um. Dabei wird das heutige Datum zur Hilfe genommen:
	 * Höchstens das nächste Jahr wird noch als das heutige Jahrhundert gezählt, ansonsten werden alle anderen größeren zweistellige Jahresangaben an das vorherige Jahrhundert gezählt.
	 * Evetueller einmaliger Jahrhundertwechsel kann somit korrekt berechnet werden.
	 * @param zweistelliges das umgewandelt werden soll
	 * @return vierstellige Jahresangabe
	 * @throws Wertangabefehler 
	 */
	public static short vierstelligesJahr (int zweistelliges) throws Wertangabefehler {
		if (aktuell==null)
			throw new Wertangabefehler ("Aktuelle Zeit konnte nicht erfasst werden.");
		Verwendbare.wertprüfung (zweistelliges, 0, 99, "Zweistelliges Jahr hat einen ungültigen Wert.");
		short heutigesJahr = aktuell.khw.jahreszahl;
		byte vergleichsjahr = zweistelligesJahr (heutigesJahr);
		if (++vergleichsjahr==100) {
			vergleichsjahr = 0;
			heutigesJahr += 100;
		}
		if (vergleichsjahr< zweistelliges)
			heutigesJahr -= 100;
		return (short) (Short.parseShort(String.valueOf(heutigesJahr).substring(0, 2) +"00") +zweistelliges);
	}
	
	/**
	 * @return die letzten zwei Stellen des Jahres
	 * @param vierstelligesJahr das durch Abschneiden der beiden ersten Werte zu einer zweistelligen Jahreszahl umgewandelt werden soll.
	 */
	public static byte zweistelligesJahr (int vierstelligesJahr) {
		return Byte.parseByte (String.valueOf(vierstelligesJahr).substring(2));
	}
}
