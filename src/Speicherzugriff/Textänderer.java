package Speicherzugriff;

import java.util.ArrayList;

import Meldung.Formatfehler;

/**
 * Fügt die übergebenenen Zeilen zu einem Text (Liste von Zeilen) zusammen und ändert dabei den Text
 * @author Attila Kiss
 */
public class Textänderer implements Ladeprozedur {
	
	public ArrayList<String> text;
	private ArrayList<String> zwischenspeicher;
	private String datei;
	
	public Textänderer (int zeilenanzahl, String datei) {
		zwischenspeicher = new ArrayList<>(zeilenanzahl);
		this.datei = datei;
	}

	public Boolean benutzt (String zeile) throws Formatfehler {
		if (zeile.startsWith("Pfad:")) {
			zeile = zeile.substring(6);
			String[] zeilenteile = zeile.split("/", 4);
			if (zeilenteile.length != 4)
				throw new Formatfehler ("Pfad besteht ungleich 4 Teilen: " +zeilenteile.length +". Fehlerhafte Datei: " +datei);
			// Dateinamenanhang weglassen
			String[] dateinamenteile = zeilenteile[3].split("[.]", 2);
			zeilenteile[3] = dateinamenteile[0];
			zeile = "Info:;" +zeilenteile[0] +"/" +zeilenteile[1] +"/" +zeilenteile[3];
		}
		zwischenspeicher.add (zeile);
		return true;
	}

	public void ende() {
		text = zwischenspeicher;
	}

	public String datei() {
		return datei;
	}
	
	/**
	 * Neue Datei wird festgelegt und der bisher zwischenspeicherte Text geleert.
	 * @param datei ist der Dateiname mit Pfad
	 */
	public void datei (String datei) {
		this.datei = datei;
		zwischenspeicher.clear();
	}
	
	

}
