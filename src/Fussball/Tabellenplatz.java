package Fussball;

/**
 * @author Attila Kiss
 */
public class Tabellenplatz implements Comparable<Tabellenplatz>{
	
	public final String teamname;
	public byte spielanzahl, geschossen, bekommen, tordifferenz, punkte;

	public Tabellenplatz (String name) {
		teamname = name;
	}
	
	public void aktualisieren (byte geschossen, byte bekommen) {
		spielanzahl++;
		this.geschossen += geschossen;
		this.bekommen += bekommen;
		tordifferenz = (byte) (geschossen -bekommen);
		if (geschossen >bekommen)
			punkte += 3;
		else if (geschossen==bekommen)
			punkte++;
	}

	// TODO Tabellenregeln von Wettbewerbsregeln prüfen lassen
	
	/**
	 * @return 1 wenn besserer Tabellenplatz oder -1 wenn schlechter oder 0 wenn gleich. Dabei werden die Punkte und die Tordifferenzen verglichen.
	 * @param ander
	 */
	public int compareTo (Tabellenplatz ander) {
		return punkte==ander.punkte && tordifferenz==ander.tordifferenz ? 0 : punkte >ander.punkte || (punkte==ander.punkte && tordifferenz >ander.tordifferenz) ? 1 : -1;
	}

}
