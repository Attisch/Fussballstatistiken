package Speicherzugriff;

import Fussball.Chronologisch.Khw;

/**
 * Beinhaltet eine Liste von Ligadateien mit Pfadnagabe, die Spiele in einer bestimmten Kalenderhalbwoche austragen.
 * @author Attila Kiss
 */
public class KhwDateien {
	
	public final Khw khw;
	public final String[] dateien;

	public KhwDateien (Khw khw, String[] dateien) {
		this.khw = khw;
		this.dateien = dateien;
	}
	
	public String toString() {
		if (dateien.length==0)
			return "";
		String text = dateien.length +" Dateien in der " +khw +":\n";
		for (String datei : dateien) {
			text += datei +"\n";
		}
		return text;
	}

}
