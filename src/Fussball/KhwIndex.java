package Fussball;

/**
 * Enthält die Indexdaten der Listen, in denen die Spiele nach Jahr, Monat und schließlich nach Kalenderhalbwoche (Khw) sortiert sind.
 * @author Attila Kiss
 */
public class KhwIndex implements Comparable<KhwIndex>{

	public byte jahr, monat, khw, spiel;
	
	public KhwIndex() {}
	
	public KhwIndex (byte jahr, byte monat, byte khw, byte spiel) {
		zuweisung (jahr, monat, khw, spiel);
	}

	public int compareTo (KhwIndex ander) {
		return jahr==ander.jahr && monat==ander.monat && khw==ander.khw ? 0 : jahr >ander.jahr || (jahr==ander.jahr && (monat >ander.monat || (monat==ander.monat && khw >ander.khw))) ? 1 : -1;
	}

	public void zuweisung (byte jahr, byte monat, byte khw, byte spiel) {
		this.jahr = jahr;
		this.monat = monat;
		this.khw = khw;
		this.spiel = spiel;
	}
	
	public void zurücksetzung() {
		byte nuller = 0;
		zuweisung (nuller, nuller, nuller, nuller);
	}

	public void kopiereNach (KhwIndex neu) {
		neu.zuweisung (jahr, monat, khw, spiel);
	}
}
