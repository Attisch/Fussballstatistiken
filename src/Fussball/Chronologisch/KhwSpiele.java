package Fussball.Chronologisch;

import Fussball.Wettbewerbsphase;
import Fussball.Spielobjekte.Spiel;
import Meldung.Programmierungsfehler;
import Meldung.Wertangabefehler;

/**
 * Enthält eine Liste mit {@link Spiel Spielen}, die chronologisch geordnet sind und in einer Kalenderhalbwoche stattfinden, die Kalenderhalbwochen-, Monats- und Jahreszahl,
 * die sich auf das erste Spiel beziehen und die Nummer der {@link Fussball.Wettbewerbsphase Wettbewerbsphase} und deren Zählernummer.
 * Durch den Umweg der Umwandlung zur {@link Khw Kalenderhalbwochen} können KhwSpiele auch dann verglichen werden, wenn die KhwSpiele höchsten im nächsten Jahr oder bis zu 98 Jahre früher stattfanden.
 * Wenn man aus dem Info des {@link Wettbewerb}s die vierstellige Jahreszahl an die Khw übergibt, ist der Vergleich die Jahresdifferenzen betreffend unbegrenzt.
 * @author Attila Kiss
 */
public class KhwSpiele {
	
	public final byte khwZahl, monatszahl, jahreszahl;
	public final Spiel[] spiele;
	
	/**
	 * Nummer der {@link Fussball.Wettbewerbsphase Wettbewerbsphase}
	 */
	private byte phasenNr;
		
	/** Die aktuelle Zahl der Phase oder {@link #NEU_GESETZTE_SPIELE} */
	public byte phasenzähler;
	
	/** Fester Phasenzählerwert für nur neu gesetzte Spiel(e) in einer Kalenderhalbwoche */
	public final static byte NEU_GESETZTE_SPIELE = 39;
	
	public KhwSpiele (byte khwZahl, byte monatszahl, byte jahreszahl, Spiel[] spiele) {
		// Die Werte werden beim Einlesen durch das Objekt Khw geprüft und übergeben
		this.khwZahl = khwZahl;
		this.monatszahl = monatszahl;
		this.jahreszahl = jahreszahl;
		this.spiele = spiele;
	}
	
	public byte phasenNr() { return phasenNr; }

	/**
	 * @return Spiel der Mannschaft in der Kalenderhalbwoche oder null
	 * @param name der Mannschaft
	 */
	public Spiel gefundenesSpiel (String name) {
		Spiel spiel = null;
		for (Spiel gs : spiele)
			if (name.equals(gs.heimteam) || name.equals(gs.auswärtsteam)) {
				spiel = gs;
				break;
			}
		return spiel;
	}
	
	// TODO Darstellung der neu gesetzten Spiele einrichten
	
	public String toString () {
		String text = "\n" +khwZahl +".Khw. ab " +Wochentag.values()[spiele[0].wochentag()] +" dem " +spiele[0].kalendertag() +"." +monatszahl +"." +jahreszahl;
		if (phasenNr >= Wettbewerbsphase.LIGASPIELPHASE.ordinal()) {
			text += " am " +phasenzähler +".Spieltag";
			if (phasenNr==Wettbewerbsphase.GRUPPENPHASE.ordinal())
				 text += " in der " +Wettbewerbsphase.GRUPPENPHASE.toString();
		}
		else if (phasenNr >= Wettbewerbsphase.ENTSCHEIDUNGSSPIELPHASE.ordinal()) {
			text += " in der " +phasenzähler +"." +Wettbewerbsphase.values()[phasenNr].toString();
		}
		text +=  ":\n";
		for (Spiel spiel : spiele)
			text += spiel.toString() +"\n";
		return text;
	}
	
	/**
	 * Nur einmalig belegbar
	 * @param phase
	 * @param zähler
	 * @throws Programmierungsfehler
	 * @throws Wertangabefehler 
	 */
	public final void phasenAngaben (Wettbewerbsphase phase, byte zähler) throws Programmierungsfehler, Wertangabefehler {
		if (phasenNr==0) {
			Allgemein.Verwendbare.wertprüfung (zähler, 1, NEU_GESETZTE_SPIELE, "Falsche Wettbewerbsphasenzahl.");
			phasenNr = (byte) phase.ordinal();
			phasenzähler = zähler;
		}
		else throw new Programmierungsfehler ("In der " +khwZahl +".Kalenderhalbwoche wurde versucht den Spielen ein zweites mal eine Phasennummer zuzuordnen.");
	}
	
}
