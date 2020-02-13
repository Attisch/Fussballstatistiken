package Fussball;

import Baumwurzelstruktur.Wurzelstruktur;

/**
 * Beinhaltet ein Gruppenzeichen und eine Liste mit Teamnamen
 */
public class Gruppe {

	public final char gruppenzeichen;
	public final Wurzelstruktur<String> teams;
	
	/**
	 * Erzeugt eine Gruppe
	 * @param gruppenzeichen
	 * @param teams
	 */
	public Gruppe(char gruppenzeichen, Wurzelstruktur<String> teams) {
		this.gruppenzeichen = gruppenzeichen;
		this.teams = teams;
	}
		
	/**
	 * Gibt die Gruppe mit allen Teams als Text zurück.
	 */
	public String toString() {
		return "Gruppe "+gruppenzeichen+": "+teams.toString();
	}
}
