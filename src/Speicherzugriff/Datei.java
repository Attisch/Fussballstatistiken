package Speicherzugriff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import Fussball.Chronologisch.Wochentag;
import Fussball.Wettbewerb;
import Fussball.Chronologisch.Khw;
import Fussball.Chronologisch.Zeitpunkt;
import Meldung.*;
import Programme.Starter;
import Visualität.Ausgabe;
import Allgemein.Verwendbare;

/**
 * Beinhaltet diverse Methoden, die zum Schreiben und Lesen einer Datei gebraucht werden.
 * @author Attila Kiss
 */
public final class Datei {
	
	private Datei() {}	
	
	//TODO Unnötige Semikolons löschen und Methoden anpassen
	/**
	 * Ändert alle Dateien im Ordner mit einer Endung bzw. ganzer Übereinstimmung im Dateinamen. Ist eigentlich für die Csv-Dateien vorgesehen.
	 * @param ordnerpfad in dessen Ordner und Unterordner die Datei(en) verändert werden
	 * @param endung des Dateinamens
	 * @throws Dateilesefehler
	 * @throws Formatfehler
	 * @throws Wertangabefehler
	 * @throws Dateischreibfehler
	 */
	public static void ändereCsv (String ordnerpfad, String endung) throws Dateilesefehler, Formatfehler, Wertangabefehler, Dateischreibfehler {
		File ordner = new File(ordnerpfad);
		ArrayList<File> dateiliste = new ArrayList<>(Starter.wbAnzahl *(Zeitpunkt.aktuell.khw.jahreszahl -Verwendbare.MIN_JAHR +1));
		Datei.filtereMUO (ordner, endungsfilter (ordner, endung), dateiliste);
		Textänderer textänderer = new Textänderer(424, "");
		System.out.println(dateiliste.size() + "Dateien zum Verändern gefunden.");
		
		for (File datei : dateiliste) {
			textänderer.datei (datei.getPath());
			Datei.ladeprozedere (textänderer);
			speicher (textänderer.text, datei.getPath());
		}
	}

	/**
	 * Gibt eine Filterbedingung zurück, die einen Ordner und Endungen der zu findenen Dateien beinhaltet
	 * @param ordner in dem gesucht werden soll
	 * @param endungen der Dateinamen. Tip: Eine vollständige Angabe des Dateinamens ist auch möglich. 
	 * @return Filter-Object
	 */
	public static FilenameFilter endungsfilter (File ordner, String... endungen) {
		return new FilenameFilter() { 
            public boolean accept (File ordner, String filename) {
            	boolean gefunden = false;
            	for (String endung : endungen) {
            		if (filename.endsWith(endung)) {
            			gefunden = true;
            			break;
            		}
            	}
            	return gefunden;
            }
        };
	}
	
	/**
	 * Gibt eine Liste mit Dateien wieder, die im Ordner durch die Dateifiltrierung sich ergibt.
	 * @param ordner ist das Object des Ordners {@link File} 
	 * @param filter ist das Object der Filtermethode {@link FilternameFilter}. Zu beachten: Wenn kein Filter angegeben wird (null), werden alle Datein (ungefiltert) übernommen.
	 * @return Liste der Dateien oder null bei Fehler
	 */
	public static File[] filtereOrdner (File ordner, FilenameFilter filter) {
		File[] dateiliste = null;
		String vonOrdner = "";
		try {
			vonOrdner = ordner.getPath();
	//      System.out.println("suche im Ordner: " +vonOrdner);
			try {
				dateiliste = ordner.listFiles (filter);
			} catch (SecurityException e) {
				throw new Verzeichnisfehler (vonOrdner, "Ordner ist nicht lesbar. Den Zugang zum Ordner ändern.");
			}
			if (dateiliste==null)
				throw new Verzeichnisfehler (vonOrdner, "Dateiordner existiert nicht.");
/*			else if (dateiliste.length==0)
				Visuelle.instanz.text ("Keine Dateien im Dateiordner (" +vonOrdner +") gefunden.");
			else Visuelle.instanz.text (dateiliste.length +" Datei(en) im Dateiordner (" +vonOrdner +") gefunden.");*/
		} catch (Exception e) {
			Ausgabe.instanz.text ("Bei der Dateifiltrierung des Ordners " +vonOrdner +" ist ein Fehler aufgetreten: "+Fehler.entstehung(e));
		}
		return dateiliste;
	}

	/**
	 * Gibt eine Liste mit Dateien wieder, die im Ordner mit Unterordner (MUO) inklusive sich durch die Dateifilterung ergibt. Siehe auch: {@link #dateiliste}
	 * @param ordner ist das Object des Ordners {@link File} 
	 * @param filter ist das Object der Filtermethode {@link FilternameFilter}
	 * @param dateiliste die mit den gefilterten Dateien gefüllt werden soll
	 * @return Liste der Dateien oder eine leere Liste
	 */
	public static void filtereMUO (File ordner, FilenameFilter filter, List<File> dateiliste) {
		
		// Alles im Ordner einlesen
		File[] pfadinhalt = filtereOrdner (ordner, null);
		if (pfadinhalt==null)
			return;
		
		// In die Unterordner gehen
        for (File inhalt : pfadinhalt)
            if (inhalt.isDirectory())
                filtereMUO (new File (ordner.getPath() +File.separator +inhalt.getName()), filter, dateiliste);
        
        // Filtere diesen Ordner
        pfadinhalt = filtereOrdner (ordner, filter);
		if (pfadinhalt !=null)
	        for (File f : pfadinhalt)
	            dateiliste.add (f);
	}
	
	/** 
	 * Ergänzt die Liste mit den Ordnernamen im gegebenen Ordner
	 * @param ordner in dem die Ordnernamen gesammelt werden
	 * @param ordnernamen ist die Liste der {@link Ordner}, die ergänzt wird
	 */
	public static void ordnernamen (File ordner, List<Ordner> ordnernamen) {
		File[] pfadinhalt = filtereOrdner (ordner, null);
		if (pfadinhalt !=null)
	        for (File inhalt : pfadinhalt)
	            if (inhalt.isDirectory())
	            	ordnernamen.add (new Ordner (inhalt.getName()));
	}
	
	/**
	 * @return Namen der Kontinenten (der Länder und deren Ligen) oder eine leere Liste
	 * @throws Wertangabefehler
	 */
	public static ArrayList<Ordner> kontinentennamen() throws Wertangabefehler {
		ArrayList<Ordner> kontinente = new ArrayList<>(6);
		ordnernamen (new File (Verwendbare.DOWNLOADS_NACH), kontinente);
		Verwendbare.wertprüfung (kontinente.size(), 0, 6, "Ungültige Anzahl an Kontinenten.");
		return kontinente;
	}
	
	/**
	 * @return Eine Liste mit Kontinentennamen, die jeweils eine Liste mit deren Ländernamen beinhalten oder eine leere Liste
	 * @throws Wertangabefehler
	 */
	public static ArrayList<Ordner> kontinente_länder() throws Wertangabefehler {
		ArrayList<Ordner> kontinente = kontinentennamen();
		for (Ordner kontinent : kontinente)
			ordnernamen (new File (Verwendbare.DOWNLOADS_NACH +"/" +kontinent.name), kontinent.unterordnerliste);
		return kontinente;
	}

	/**
	 * @return Eine Liste mit Kontinentennamen, die jeweils eine Liste mit deren Ländernamen und die wiederum eine Liste mit deren Ligennamen beinhalten oder eine leere Liste
	 * @throws Wertangabefehler
	 */
	public static ArrayList<Ordner> kontinente_länder_ligen() throws Wertangabefehler {
		ArrayList<Ordner> kontinente = kontinente_länder();
		for (Ordner kontinent : kontinente)
			for (Ordner land : kontinent.unterordnerliste)
				ordnernamen (new File (Verwendbare.DOWNLOADS_NACH +"/" +kontinent.name +"/" +land.name), land.unterordnerliste);
		return kontinente;
	}

	public static String[] ligennamen() throws Wertangabefehler {
		short max = Starter.wbAnzahl;
		String[] ligen = new String[max];
		short ligenNr = 0;
		ArrayList<Ordner> kontinente = Datei.kontinente_länder_ligen();
		for (Ordner kontinent : kontinente) {
			String[] pfäde = kontinent.pfäde();
			for (short s = 0; s< pfäde.length; s++)
				ligen[ligenNr++] = pfäde[s];
		}
		return ligen;
	}
	
	/**
	 * Ordnet die Downloads aus einem Pfad in einem neuen Pfad und dessen entsprechenden Unterverzeichnissen ein.
	 * Dabei wird die Kalenderwochenansicht durch eine Kalenderhalbwochenansicht ersetzt,
	 * Spiele gelöscht, die als verschoben gekennzeichnet sind und in der gleichen Kalenderhalbwoche schon neu angesetzt wurden und somit doppelt im Text existieren.
	 * Außerdem werden alle Spiele mit einem Wochentag versehen.
	 * @param von Ordner
	 * @param nach Ordner
	 */ 
	public static void downloadsEinordnen (String von, String nach) {
		Downloadeinordner einordner = new Downloadeinordner("");
		File datenpfad;
		File downloadOrdner;
		File[] downloadliste = null;
		String[] zeilenteile;
		String jahr = "", datei = "";
		int nr = 0;
		boolean betradarDatei;
		try {																		// Dateien sammeln
			downloadOrdner = new File (von);
			downloadliste = filtereOrdner (downloadOrdner, endungsfilter (downloadOrdner, ".csv"));
			Ausgabe.instanz.text (downloadliste.length +" Spiele gefunden.");
		} catch (Exception e) {
			Ausgabe.instanz.text ("Fehler beim Einlesen der Dateien, die eingeordnet werden sollen, aufgetreten: " +Fehler.entstehung(e));
			return;
		}
		for (File s : downloadliste) {												// Ausgesuchte Dateien prüfen und ggf. einlesen
			try {
				betradarDatei = true;
				datei = s.getName();
				if (datei.length()< 11)
					betradarDatei = false;
				if (betradarDatei) {
					if (datei.substring (datei.length() -5, datei.length() -4).equals(")"))
						nr = 7;
					else nr = 4;
					datei = datei.substring (0, datei.length() -nr);				// Dateiname ohne Endung und Klammerangaben
					jahr = datei.substring (datei.length() -4);
					try {
						Short.parseShort (jahr);
					} catch (NumberFormatException e) {
						betradarDatei = false;
					}
				}
				if (betradarDatei) {
					einordner.datei = s.getPath();
					Datei.ladeprozedere (einordner);
					einordner.text.add ("Kürzeln:;u.U. = unbestimmte Uhrzeit");
					einordner.text.add (";v.S. = verschobenes Spiel");
					einordner.text.add (";f.E. = festgelegtes Ergebnis");
					einordner.text.add (";f.H. = festgelegter Heimsieg");
					einordner.text.add (";f.A. = festgelegter Auswärtssieg");
					einordner.text.add (";n.V. = nach Verlängerung");
					einordner.text.add (";n.E. = nach Elfmeterschießen");
					einordner.text.add (";KHW = Kalenderhalbwochen");
					zeilenteile = einordner.infoteile;								// Existenz der Ordner prüfen und neu erzeugen, falls nicht vorhanden
					zeilenteile[2] = zeilenteile[2].substring(0, zeilenteile[2].length() -5);
					datenpfad = new File (nach);
					try {
						if (!datenpfad.isDirectory())
							Files.createDirectory (datenpfad.toPath());
						datei = nach;
						for (int b = 0; b< 3; b++) {
							datei += "/" +zeilenteile[b];
							datenpfad = new File (datei);
							if (!datenpfad.isDirectory())
								Files.createDirectory (datenpfad.toPath());
						}
					} catch (SecurityException e1) {
						throw new Verzeichnisfehler (datenpfad.getPath(), "Datenzielordner ist nicht lesbar. Zugang zum Ordner bitte freischalten.");
					}
					datei += "/" +zeilenteile[2] + " " +jahr +".csv";				// Datei im neuen Pfad speichern und eventuell umbenennen
					speicher (einordner.text, datei);								//  (bei Wettbewerbnamensgleichheit die eingeklammerte Nummer und das Leerzeichen davor entfernen)
					Ausgabe.instanz.text ("Datei erfolgreich gespeichert: " +datei);
				}
				else Ausgabe.instanz.text ("Unbekannte Datei übersprungen: " +datei);
			} catch (Exception e) {
				Ausgabe.instanz.text ("Die CSV-Datei konnte nicht verarbeitet werden: " +datei +". " +Fehler.entstehung(e));
			}			
		}	
	}

	
	/**
	 * Lädt eine Datei zeilenweise und führt nach jeder Zeile eine {@link Ladeprozedur} aus. Abbruch der Einlesung, wenn die Ladeprozedur es signalisiert.
	 * @param prozedur
	 * @throws Dateilesefehler
	 */
	public static void ladeprozedere (Ladeprozedur prozedur) throws Dateilesefehler {
		String zeile = null;
		try (BufferedReader leser = new BufferedReader (new FileReader (prozedur.datei()));) {
			while ((zeile = leser.readLine()) !=null)
				if (!prozedur.benutzt(zeile))
					break;
			prozedur.ende();		
		} catch (Exception e) {
			String meldung = "Datei konnte nicht geladen werden. ";
			if (zeile !=null)
				meldung = "Fehler in der Zeile: " +zeile +". "; 
			throw new Dateilesefehler (prozedur.datei(), meldung +Fehler.pfadteilangabe(e, "Speicherzugriff.Datei.ladeprozedere"));
		}
	}
	
	/**
	 * Lädt einen Wettbewerb und gibt sie zurück.
	 * @param datei des Wettbewerbs
	 * @return Wettbewerb
	 * @throws Fehler 
	 */
	public static Wettbewerb ladeWb (String datei) throws Fehler {
		Wettbewerbleser wbLeser = new Wettbewerbleser (datei);
		Datei.ladeprozedere (wbLeser);
		return wbLeser.wettbewerb();
	}
	
	
	/**
	 * Speichert den angegebenen Text in der angegebenen Datei mit Pfadangabe
	 * @param text der gespeichert werden soll
	 * @param datei ist der Dateiname mit Pfad, in dem es gespeichert wird
	 * @throws Dateischreibfehler 
	 */
	public static void speicher (List<String> text, String datei) throws Dateischreibfehler {
		try (PrintWriter schreiber = new PrintWriter (new BufferedWriter (new FileWriter(datei)));) {
			for (String zeile : text)
				schreiber.println (zeile);
		} catch (IOException e) { 
			throw new Dateischreibfehler (datei, e.getMessage()); 
		}
	}

	/**
	 * Testet, ob in der angegebenen Zeile ein Spiel ist, dabei wird der erste und der zweite Schrägstrich in der Datumsangabe geprüft.
	 * Wenn es ein Spiel ist, werden die Daten in einer Liste zurückgegeben, wenn nicht: null. 
	 * @param zeile
	 * @param spaltenanzahl ist das vorgegebene Anzahl der Spalten in einer Zeile, die übereinstimmen müssen
	 * @return Liste mit den Daten des Spieles oder null
	 * @throws Formatfehler wenn die Spaltenanzahl der Zeile ungleich der vorgegebenen ist
	 */
	public static String[] spiel (String zeile, int spaltenanzahl) throws Formatfehler {
		String[] zeilenteile = null;
		if (zeile.length() >5 && zeile.substring(2, 3).equals("/") && zeile.substring(5, 6).equals("/"))
			if ((zeilenteile = zeile.split(";", spaltenanzahl)).length != spaltenanzahl)
				throw new Formatfehler ("Es sind ungleich " +spaltenanzahl +" Spalten in der Zeile: " +zeile);
		return zeilenteile;
	}
	
	/**
	 * Testet, ob in der angegebenen Zeile ein {@link #spiel Spiel} ist. Wenn es ein Spiel ist, werden die Ergebnisse und die Ergeignisse geprüft.
	 * Wenn alle richtig sind, werden sie in einer Liste zurückgegeben, wenn nicht: null.
	 * @param zeile ist die zu prüfende Zeile
	 * @param spaltenanzahl ist das vorgegebene Anzahl der Spalten in einer Zeile, die übereinstimmen müssen
	 * @param startposition ist die nullbasierte Position, an der die {@link #ergebnisprüfung} beginnt und dann zwei Positionen weiter die {@link #ereignisprüfung} stattfindet.
	 * @return Liste mit den String-Daten des Spieles oder null
	 * @throws Formatfehler wenn die Spaltenanzahl der Zeile ungleich der vorgegebenen ist
	 */
	public static String[] spielprüfung (String zeile, int spaltenanzahl, int startposition) throws Formatfehler {
		String[] zeilenteile = null;	
		if ((zeilenteile = spiel (zeile, spaltenanzahl)) != null) {
			ergebnisprüfung (zeilenteile, startposition);
			ereignisprüfung (zeilenteile[startposition+2]);
		}
		return zeilenteile;
	}
	
	/**
	 * Prüft die Info-Angabe in der CSV-Datei einer Liga.
	 * @param info ist zu prüfen
	 * @return die 3 Teile der Info oder ein Formatfehler wird zuvor ausgelöst
	 * @throws Formatfehler
	 */
	static String[] infoprüfung (String info) throws Formatfehler {
		String[] infoteile;
		if ((infoteile = info.split("/")).length !=3)
			throw new Formatfehler ("Info besteht nicht aus genau drei Teilen: " +info);
		if (infoteile[2].length()< 6)
			throw new Formatfehler ("Der letzte Teil von Info ist zu kurz: " +info);
		try {
			Short.parseShort(infoteile[2].substring(infoteile[2].length()-4));
		} catch (NumberFormatException e) {
			throw new Formatfehler ("Info hat keine Jahresangabe: " +info);
		}
		return infoteile;
	}
	
	

	/**
	 * Prüft, ob im Zeilenteil ein Ereignis sich befindet. Ein Ereignis ist stets sowohl zweiteilig und durch einen Punkt getrennt und abgeschlossen als auch aus 4 Zeichen bestehend.
	 * Gibt die Buchstaben des Ereignisses ohne Punkttrennung zurück oder null
	 * @param zeilenteil ist das Ereignisteil der Zeile
	 * @return Ereignis bestehend aus zwei Buchstaben ohne Punkttrennung oder null
	 * @throws Formatfehler wenn anstatt des Ereinisses was anderes steht
	 */
	static String ereignisprüfung (String zeilenteil) throws Formatfehler {
		String ereignis = null;
		if (zeilenteil.length() >0) {
			String[] ereignisteile = zeilenteil.split("[.]");
			if (zeilenteil.length() !=4 || (ereignisteile.length !=2 || ereignisteile[0].length() !=1 || ereignisteile[1].length() !=1))
				throw new Formatfehler ("An der Stelle des Ereignisses steht was anderes.");
			ereignis = ereignisteile[0] +ereignisteile[1];
		}
		return ereignis;
	}

	/**
	 * Prüft, ob in den Zeilenteilen beide Ergebnisse vorliegen (Halbzeit- und Endstandergebnis). Dabei werden beide Ergebnisse auch auf deren Format überprüft.
	 * @param zeilenteile des Spieltextes
	 * @param startposition ist die nullbasierte Position des Spieltextes, an der die Ergebnisse zuerst geprüft werden 
	 * @return liste der geschossenen Tore oder eine Liste teils oder ganz mit -1 gefüllt.
	 * @throws Formatfehler falls was anderes als Ergebnisse an deren Stelle sich befinden 
	 */
	static byte[] ergebnisprüfung (String[] zeilenteile, int startposition) throws Formatfehler {
		String[] ergebnisseAlsText;	
		byte[] ergebnisse = new byte[4];
		byte ergebnisNr;
		for (int i = startposition; i< startposition+2; i++) {
			if (i==startposition)
				ergebnisNr = 0;
			else ergebnisNr = 2;
			if (zeilenteile[i].length() >0) {
				try {
					if ((ergebnisseAlsText = zeilenteile[i].split(":", 2)).length==2) {
						ergebnisse[ergebnisNr] = Byte.parseByte (ergebnisseAlsText[0]);
						ergebnisse[ergebnisNr+1] = Byte.parseByte (ergebnisseAlsText[1]);
					} else throw new Formatfehler ("");
				} catch (NumberFormatException | Formatfehler f) {
					throw new Formatfehler ("An der Stelle des Ergebnisses steht was anderes.");
				}
			} else {
				ergebnisse[ergebnisNr] = Verwendbare.UNBEKANNT;
				ergebnisse[ergebnisNr+1] = ergebnisse[ergebnisNr];
			}
		}
		return ergebnisse;
	}

	/**
	 * Prüft den Inhalt der Quoten und gibt sie als eine Liste von 6 Zahlen zurück. 1X2-Quoten, wobei jeweils zwei Zahlen die Vor- und Nachkommazahlen einer Quote beinhalten.
	 * Wenn keine Quoten vorhanden sind, beinhaltet die Liste lauter 0.
	 * @param zeilenteile
	 * @return eine Liste von sechs Zahlen
	 * @throws Formatfehler
	 */
	static short[] quotenprüfung(String[] zeilenteile) throws Formatfehler {
		short[] quoten = new short[6];
		String[] quotenteile;
		if (zeilenteile.length <= Verwendbare.SPALTENANZAHL) {
			if (!zeilenteile[8].isEmpty()) {
				final String fehler = "An der Stelle der Quoten steht was anderes: " +zeilenteile[8] +" " +zeilenteile[9] +" " +zeilenteile[10];
				for (byte b = 0; b< 3; b++) {
					try {
						quotenteile = zeilenteile[b+8].split("[.]", 2);
						if (quotenteile.length !=2)
							throw new Formatfehler (fehler);
						quoten[b*2] = Short.parseShort (quotenteile[0]);
						quoten[b*2+1] = Short.parseShort (quotenteile[1]);
					} catch (NumberFormatException e) {
						throw new Formatfehler (fehler);
					}
				}
			}
		} else throw new Formatfehler ("Spaltenanzahl liegt über den Limit: " +Verwendbare.SPALTENANZAHL);
		return quoten;
	}
	
	/**
	 * Ergänzt die Zeilenteile durch einen Wochentagangabe und gibt die Daten als ganzer String zurück
	 * @param zeilenteile
	 * @param wochentag als nullbasierte Nummer
	 * @return Text eines Spieles mit Wochentagangabe
	 * @throws Formatfehler
	 * @throws Wertangabefehler
	 */
	static String ergänzeWochentag (String[] zeilenteile, byte wochentag) throws Formatfehler, Wertangabefehler {
		String mitWochentag = "";
		for (byte b = 0; b< zeilenteile.length; b++) {
			if (b==1)
				mitWochentag += Wochentag.values()[wochentag].abkürzung() +";";
			mitWochentag += zeilenteile[b];
			if (b< zeilenteile.length-1)
				mitWochentag += ";";
		}
		return mitWochentag;
	}

	/**
	 * Gibt die Angabe über das Jahr in angegebener Datei mit Pfadangabe zurück.
	 * @param datei
	 * @return vierstellige Jahresangabe
	 * @throws Formatfehler falls das vierstellige Jahr nicht ordnungsgemäß im Dateinamen enthalten ist
	 */
	private static short jahr (String datei) throws Formatfehler {
		short jahr = 0;
		try {
			jahr = Short.parseShort(datei.substring (datei.length()-8, datei.length()-4));
		} catch (NumberFormatException e) {
			throw new Formatfehler("Das vierstellige Jahr ist nicht ordnungsgemäß im Dateinamen enthalten: " +datei);
		}
		return jahr;
	}

	/**
	 * Gibt eine Meldung aus, wenn der Name der Mannschaft mehr als 22 Zeichen hat.
	 * @param zeilenteile
	 * @param datei ist der Dateiname mit Pfadangabe
	 */
	public static void namenlängenprüfung (String[] zeilenteile, List<String[]> langeNamen, String datei) {		
		for (byte b = 2; b< 4; b++) {
			if (zeilenteile[b].length() >22) {					// Wenn der Name mehr als 22 Zeichen hat
				boolean checker = false;						// Checkt zuerst, ob der Name schon gekürzt wurde
				switch (zeilenteile[b]) {
					case "AS La Jeunesse D Esch/Alzette": zeilenteile[b] = "Jeunesse Esch";	checker = true; break;
					case "Daco-Getica": zeilenteile[b] = "Daco-Getica";	checker = true; break;
					case "Maccabi Achi Nazareth FC": zeilenteile[b] = "Maccabi Achi Nazareth"; checker = true; break;
					case "US Triestina Calcio 1918": zeilenteile[b] = "Triestina Calcio"; checker = true; break;
					case "Acs FC Academica Clinceni": zeilenteile[b] = "Academica Clinceni"; checker = true; break;
					default: break;
				}
				if (!checker) {									// Checkt dann, ob der Name der aktuellen Datei schon aufgenommen worden ist
					String[] paarung = {zeilenteile[b],datei};
					for (String[] paar : langeNamen)
						if (paar[0].equals(zeilenteile[b]) && paar[1].equals(datei))
							checker = true;
					if (!checker) {
						Ausgabe.instanz.text ("Zu langer Name der Mannschaft \""+ zeilenteile[b] + "\" in der Datei: " + datei);
						langeNamen.add(paarung);
					}
				}
			}
		}
	}
	
	/**
	 * Gibt eine Liste mit Dateinamen samt Pfad zurück, unter der die Wettbewerbe zu finden sind, die an gegebener Kalenderhalbwoche Spiele ausgetragen haben/werden
	 * @param gesuchteKhwNr ist die Nummer der Khw
	 * @param gesuchterMonat ist die Nummer des Monats, in dem die Khw ist
	 * @param gesuchtesJahr ist die Nummer des Jahres, in dem die Khw ist
	 * @return Liste mit Dateinamen samt Pfad
	 * @throws Wertangabefehler
	 * @throws Dateilesefehler
	 * @throws Formatfehler
	 */
/*	public static String[] wettbewerbsauswahl (byte gesuchteKhwNr, byte gesuchterMonat, short gesuchtesJahr) throws Wertangabefehler, Dateilesefehler, Formatfehler {
		ArrayList<File> dateien = Datei.wettbewerbsdateien ("2019.csv", 36, 1);
		ArrayList<String> auswahl = new ArrayList<>(36);
		KhwSuche gesuchteKhw = new KhwSuche (gesuchteKhwNr, gesuchterMonat, gesuchtesJahr);
		Khw allernäheste = new Khw();
		int vergleich = 0;
		boolean gleicheKhw, näher, einmaligGleich = false;
		for (File datei : dateien) {
			näher = false;
			gleicheKhw = false;
			gesuchteKhw.bereit();
			Datei.ladeprozedere (datei.getPath(), gesuchteKhw);
			System.out.println (datei.getPath() +": " +gesuchteKhw.nähesteKhw.toString());
			if (gesuchteKhw.compareTo(gesuchteKhw.nähesteKhw)==0) {		// Wenn das Gesuchte gefunden wurde
				if (!einmaligGleich) {
					einmaligGleich = true;
					auswahl.clear();
				}
				gleicheKhw = true;
			}
			if (!einmaligGleich) {
				if (gleicheKhw || (allernäheste.zahl!=0 && ((vergleich = gesuchteKhw.nähesteKhw.compareTo(allernäheste))< 0 && gesuchteKhw.compareTo(allernäheste)< 0) ||
						vergleich >0 && gesuchteKhw.compareTo(allernäheste) >0)) {
						auswahl.clear();								// Wenn nähere Khw gefunden, bisherige Auswahl leeren.
						näher = true;
				}
			}
			if (gleicheKhw || allernäheste.zahl==0 || näher)
				gesuchteKhw.nähesteKhw.kopiereNach(allernäheste);
			else continue;												// eine frühere Khw gefunden
			auswahl.add(datei.getPath());
		}
		System.out.println("Näheste Khw: " +allernäheste.toString());
		return auswahl.toArray(new String[auswahl.size()]);
	}*/

	/**
	 * Übernimmt eine Liste für Zahlen und befüllt sie mit Teilen eines Termins: 0-2=Datum oder mit 3=Wochentag oder auch mit 4-5=Uhrzeit oder nur mit 3-4=Uhrzeit.
	 * Falls die Uhrzeit unbekannt ist, wird jeweils -1 für Stunde und Minute zugeordnet.
	 * @param zeilenteile ist eine Liste Texteilen aus der Zeile, aus der ein Termin ausgelesen wird.
	 * @param terminteile ist eine Liste, die mit 3-6 Zahlen vollständig neu befüllt wird
	 * @throws Formatfehler
	 * @throws Wertangabefehler 
	 */
	static void terminteile (String[] zeilenteile, byte[] terminteile) throws Formatfehler, Wertangabefehler {
		byte uhrzeitindex = 1;
		boolean unbekannteUhrzeit = false;
		Verwendbare.wertprüfung (terminteile.length, 3, 6, "In der Klasse 'Datei' der Methode 'terminteile' hat der Parameter 'terminteile' eine ungültige Listengröße.");
		
		// Datum
		String[] zeilenteile2 = zeilenteile[0].split("/", 3);
		if (zeilenteile2.length !=3) 
			throw new Formatfehler ("Datum: " +zeilenteile[0]);
		terminteile = textZuBytes (zeilenteile2, terminteile, 0);
		if (terminteile.length >3) {
			if (terminteile.length !=5) {
				
				// Tagesangabe
				terminteile[3] = Wochentag.nummer (zeilenteile[1]);
				uhrzeitindex = 2;
			} 
			if (terminteile.length !=4) {		
				
				// Uhrzeit
				zeilenteile2 = zeilenteile[uhrzeitindex].split(":", 2);
				if (zeilenteile2.length !=2) {
					zeilenteile2 = zeilenteile[uhrzeitindex].split("[.]", 2);
					if (zeilenteile2.length !=2)
						throw new Formatfehler ("Uhrzeit: " +zeilenteile[uhrzeitindex]);
					else {
						unbekannteUhrzeit = true;
						terminteile[terminteile.length-2] = -1;
						terminteile[terminteile.length-1] = -1;
					}
				}
				if (!unbekannteUhrzeit)
					terminteile = textZuBytes (zeilenteile2, terminteile, terminteile.length-2);
			}  
		}
	}
	
	private static byte[] textZuBytes (String[] text, byte[] zahlen, int startindex) throws Formatfehler {
		try {	
			for (int i = 0; i< text.length; i++)
				zahlen[startindex+i] = Byte.parseByte(text[i]);
		} catch (NumberFormatException e) {
			throw new Formatfehler ("Der Termin besteht nicht nur aus Zahlen: "+e.getMessage());
		}
		return zahlen;
	}
	
	/**
	 * Schaut, ob in dem angegebenen Teil der Zeile das Spiel verschoben wurde
	 * @param zeilenteil
	 * @return true oder false
	 */
	static boolean verschobenesSpiel (String zeilenteil) {
		if (zeilenteil.equals("v.S."))
			return true;		
		return false;
	}	
	
	/**
	 * Alle im Ordnerpfad befindlichen Wettbewerbsdateien mit den angegebenen Endungen werden in der Dateiliste aufgenommen
	 * @param dateiliste
	 * @param ordnerpfad in dem gesucht wird
	 * @param endungen sind Möglichkeiten wie eine Datei enden kann
	 */
	public static void wbDateien (List<File> dateiliste, String ordnerpfad, String... endungen) {
		File ordner = new File (ordnerpfad);
		Datei.filtereMUO (ordner, Datei.endungsfilter (ordner, endungen), dateiliste);
	}
	
	/**
	 * @return Gibt den Namen des Wettbewerbs wieder
	 * @param datei ist der Dateiname samt Pfad
	 * @throws Formatfehler
	 */
	public static String wbName (String datei) throws Formatfehler {
		String[] dateiteile = Datei.separierterPfad (datei);
		String dateiname = dateiteile[dateiteile.length-1];
		if (dateiname.length()< 10)
			throw new Formatfehler ("Dateiname besteht aus weniger als 10 Zeichen.");
		return dateiname.substring (0, dateiname.length() -9);
	}
	
	// TODO
	public static ArrayList<File> nationaleLigaDateien (String ordnerpfad, String endung, int ordneranzahl, int dateienanzahl) {
		File ordner = new File (ordnerpfad);
		ArrayList<File> dateiliste = new ArrayList<>(ordneranzahl * dateienanzahl);
		Datei.filtereMUO (ordner, Datei.endungsfilter(ordner, endung), dateiliste);
		return dateiliste;
	}

	/**
	 * @return eine Liste mit 4 Torangaben und eventueller Anmerkung 
	 * @param toreText ist der Text mit den Toren
	 * @throws Formatfehler falls zu viele oder zu wenige Torangaben gibt
	 */
	static String[] tore (String toreText) throws Formatfehler {
		String[] tore = toreText.split("[(]");							// Eventuelle Erweiterungen abschneiden
		String erweiterung = null;
		byte angabenanzahl = 4;											// Anzahl der Angaben die letzendlich zurückgegeben werden	
		if (tore.length >1) {
			erweiterung = tore[1].substring(0, tore[1].length()-1);		// Ohne Klammern die Erweiterung übernehmen
			angabenanzahl = 5;
		}
		String[] HZendstand = tore[0].split(" ", 2);
		if (HZendstand.length != 2)
			throw new Formatfehler ("Ungültige Anzahl an Halbzeit- und Endergebnissen: " +HZendstand);
		String[] ergebnis = HZendstand[0].split(":", 2);							// Halbzeitergebnisse
		if (ergebnis.length != 2)
			throw new Formatfehler ("Ungültige Anzahl an Toren des Halbzeitergebnisses. " +ergebnis);
		tore = new String[angabenanzahl];
		tore[0] = ergebnis[0];
		tore[1] = ergebnis[1];
		ergebnis = HZendstand[1].split(":", 2);							// Endergebnisse
		if (ergebnis.length != 2)
			throw new Formatfehler ("Ungültige Anzahl an Toren des Endergebnisses. " +ergebnis);
		tore[2] = ergebnis[0];
		tore[3] = ergebnis[1];
		if (angabenanzahl==5)
			tore[4] = erweiterung;
		return tore;
	}
	
	/**
	 * @return Teile des Dateipfads zurück
	 * @param datei mit Pfad
	 */
	public static String[] separierterPfad (String datei) {
		String sep = File.separator;
		String[] dateiteile = null;
		try {
			dateiteile = datei.split(sep);
		} catch (Exception e) {
			dateiteile = datei.split("\\\\");
		}
		return dateiteile;
	}
	
	/**
	 * @return null, wenn die Dateiteile nicht aus 5 Teilen bestehen, ansonsten die Teile 2-4 als Pfadangabe
	 * @param datei
	 */
	public static String infoOhneJahr (String datei) {
		String[] dateiteile = separierterPfad (datei);
		if (dateiteile.length !=5)
			return null;
		else return dateiteile[1] +" → " +dateiteile[2] +" → " +dateiteile[3];
	}
	
	public static boolean gleicheWb (String pfadteil1, String pfadteil2) {
		if (infoOhneJahr(pfadteil1).equals (infoOhneJahr(pfadteil2)))
			return true;
		return false;
	}
	
	/**
	 * Ergänzt die Liste der {@link KhwDateien} mit aktuellen KhwDateien 
	 * @param alle Dateien, die ergänzt werden
	 * @param khw ist die Kalenderhalbwoche
	 * @param aktuelle Dateien, die hinzugefügt werden
	 */
	private static void ergänzeKhwDateien (ArrayList<KhwDateien> alle, Khw khw, ArrayList<String> aktuelle) {
		if (aktuelle.size() !=0)
			alle.add (new KhwDateien (khw, aktuelle.toArray(new String[aktuelle.size()])));
	}
	
	/**
	 * @return Liste der aktuellen Dateien, die die aktuell wichtigen Kalenderhalbwochen beinhalten. Dabei ist es eine Liste von Listen, die höchsten 2 Elemente haben kann:
	 * Dateilisten mit näheste und nächstnäheste Khwn
	 * @throws Wertangabefehler
	 * @throws Dateilesefehler
	 * @throws Formatfehler
	 */
	public static KhwDateien[] aktuelleKhwDateien() throws Wertangabefehler, Dateilesefehler, Formatfehler {
		short heutigesJahr = Zeitpunkt.aktuell.khw.jahreszahl;
		LinkedList<File> dateien = new LinkedList<>();
		KhwDateienSucher khwSuche = new KhwDateienSucher ("", Zeitpunkt.aktuell.khw);
		String datei1, datei2;
		
		// Die Pfäde der Wettbewerbsdateien mit -wenn vorhanden- aktuellem Anfangsjahr und mit dessem Vorgängerjahr laden.
		Datei.wbDateien (dateien, Verwendbare.DOWNLOADS_NACH, heutigesJahr +".csv", (heutigesJahr-1) +".csv"); //TODO Monatsbedingung einrichten macht Sinn?
		ListIterator<File> it = dateien.listIterator();
		
		// Nur die aktuelle Datei einer Liga behalten, wenn zwei Dateien zur Auswahl stehen
		while (it.hasNext()) {
			datei1 = it.next().getPath();
			if (it.hasNext()) {
				datei2 = it.next().getPath();
				if (gleicheWb (datei1, datei2)) {
					it.previous();
					it.previous();
					it.remove();
					it.next();
				}
				else it.previous();
			}
		}
/*		System.out.println(dateien.size());
		for (File datei : dateien)
			System.out.println (datei.getPath());*/
		
		// Die Ligen nach der nähesten und -wenn es Sinn macht- nach der nächstnähesten Khw filtrieren
		for (File datei : dateien) {
			khwSuche.datei = datei.getPath();
			Datei.ladeprozedere (khwSuche);
//			System.out.println (khwSuche.datei +"; Khwn:"+ khwSuche.aktuelleKhw() +" mit " +khwSuche.aktuelleDateien().size()
//								+" Dateien und " +khwSuche.nächstaktuelleKhw() +" mit " +khwSuche.nächstaktuelleDateien().size() +" Dateien.");
		}

		// Ergebnisse sammeln
		ArrayList<KhwDateien> wichtigeDateien = new ArrayList<>(2);
		ergänzeKhwDateien (wichtigeDateien, khwSuche.aktuelleKhw(), khwSuche.aktuelleDateien());
		ergänzeKhwDateien (wichtigeDateien, khwSuche.nächstaktuelleKhw(), khwSuche.nächstaktuelleDateien());
		return wichtigeDateien.toArray(new KhwDateien[wichtigeDateien.size()]);
	}
	

}

