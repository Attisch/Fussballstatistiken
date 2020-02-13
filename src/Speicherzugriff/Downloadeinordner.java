package Speicherzugriff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import Fussball.Chronologisch.Zeitpunkt;
import Meldung.Formatfehler;
import Meldung.Wertangabefehler;


/**
 * Enthält eine {@link Ladeprozedur}, die den Download mit gewissen Angaben ergänzt bzw. verändert: Kalenderhalbwochenansicht anstatt Kalenderwochenansicht, Namenlängenprüfung, Wochentagergänzung,
 * Überprüfung der verschobenen Spiele, nicht dass in einer und derselben Kalenderhalbwoche (Khw) das Spiel neu gesetzt worden ist und somit zwei Angaben über ein und demselben Team existieren.
 * @author Attila Kiss
 */
public class Downloadeinordner implements Ladeprozedur {

	public LinkedList<String> text = new LinkedList<>();
	public String datei;
	public String[] infoteile;
	private ArrayList<String[]> langeNamen = new ArrayList<String[]>();
	private LinkedList<String[]> verschobeneSpiele = new LinkedList<>();
	private ListIterator<String> textIT;
	private ListIterator<String[]> verschobenIT;
	private Zeitpunkt aktuellerTermin = null, vorherigerTermin = null;
	private boolean ersteZeile = true, infoFolgt;
	
	public Downloadeinordner (String datei) {
		this.datei = datei;
	}

	public void ende() {
		langeNamen.clear();
		verschobeneSpiele.clear();
		textIT = text.listIterator();
		verschobenIT = verschobeneSpiele.listIterator();
		aktuellerTermin = vorherigerTermin = null;
		ersteZeile = true;
		infoFolgt = false;
	}
	
	public Boolean benutzt (String zeile) throws Formatfehler, Wertangabefehler {
		byte[] terminteile = new byte[3];
		final byte SPALTENANZAHL = 10;
		String[] zeilenteile, spiel = null;

		if (ersteZeile) {															// "Tag" als Spaltenname hinzufügen
			zeilenteile = zeile.split(";",2);
			if (zeilenteile.length< 2)
				throw new Formatfehler ("1.Zeile ist zu kurz: " +zeile);
			zeile = zeilenteile[0] +";Tag;" + zeilenteile[1];
			text.clear();
			ersteZeile = false;
		}
		else if (zeile.length() >4 && zeile.substring(0, 5).equals("Woche"))		// Kalenderwochenangabe ignorieren
			return true;
		else if ((zeilenteile = Datei.spielprüfung(zeile, SPALTENANZAHL, SPALTENANZAHL-6)) !=null) {	// Wenn es ein Spiel ist
			Datei.namenlängenprüfung (zeilenteile, langeNamen, datei);				// Namenlängenprüfung
			if (aktuellerTermin != null) {											// Wenn aktueller Termin existiert, wird er zum vorherigen Termin
				if (vorherigerTermin==null)
					try {
						vorherigerTermin = aktuellerTermin.clone();
					} catch (CloneNotSupportedException e) {
						throw new Formatfehler ("Ungültiger Termin: " +aktuellerTermin.toString());
					}
				else aktuellerTermin.kopiereNach(vorherigerTermin); 
			}
			Datei.terminteile (zeilenteile, terminteile);							// Datum einlesen
			if (aktuellerTermin==null)												// Datum dem aktuellem Termin zuweisen
				aktuellerTermin = new Zeitpunkt (terminteile[0], terminteile[1], terminteile[2]);
			else {
				aktuellerTermin.datumzuweisung (terminteile[0], terminteile[1], terminteile[2]);
				aktuellerTermin.unbekannterWochentag();
				aktuellerTermin.berechneKhw (Zeitpunkt.aktuell);					// Die Kalenderhalbwoche und den Wochentag des aktuellen Termins berechnen
			}
			if (vorherigerTermin==null || vorherigerTermin.khw.zahl !=aktuellerTermin.khw.zahl) {
				text.add (aktuellerTermin.khw.zahl +".KHW");						// Neue KHW setzen
				verschobeneSpiele.clear();
			}
			if (Datei.verschobenesSpiel (zeilenteile[SPALTENANZAHL-4])) {			// Verschobene Spiele rückwärts in der Liste der Spiele prüfen, ob sie früher neu gesetzt worden sind
				textIT = text.listIterator(text.size());
				while (textIT.hasPrevious() && (spiel = textIT.previous().split(";", SPALTENANZAHL-5)).length >2 && (spiel[2].equals(zeilenteile[2]) || spiel[2].equals(zeilenteile[3])));
				if (!textIT.hasPrevious() || spiel.length< 3)
					verschobeneSpiele.add (zeilenteile);							// In die Liste der verschobenen Spiele aufnehmen
				else return true;
			}
			else if (!verschobeneSpiele.isEmpty()) {								// Verbliebene verschobene Spiele vorwärts in der Liste der Spiele prüfen
				verschobenIT = verschobeneSpiele.listIterator();
				while (verschobenIT.hasNext()) {
					spiel = verschobenIT.next();
					if (spiel[2].equals(zeilenteile[2]) || spiel[2].equals(zeilenteile[3])) {	// Wenn Teamname doppelt im gleichem KHW
						textIT = text.listIterator(text.size());								// Zurückgehen und löschen
						while ((spiel = textIT.previous().split(";", SPALTENANZAHL-5)).length >2 && (spiel[2].equals(zeilenteile[2]) || spiel[2].equals(zeilenteile[3])));
						textIT.remove();
						verschobenIT.remove();
						break;
					}
				}
				while (verschobenIT.hasPrevious())									// Zurück zum Ausgangspunkt
					verschobenIT.previous();
			}
			zeile = Datei.ergänzeWochentag (zeilenteile, aktuellerTermin.wochentag());
		}
		else if (zeile.isEmpty() && !infoFolgt)
			infoFolgt = true;
		else if (infoFolgt && zeile.startsWith("Info:;")) {
			String info = zeile.substring(6);
			infoteile = Datei.infoprüfung (info);
		}
		else throw new Formatfehler ("Unbekannter Inhalt: " +zeile);
		text.add(zeile);
		return true;
	}
	
	public String datei() {
		return datei;
	}
}
