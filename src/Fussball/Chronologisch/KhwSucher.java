package Fussball.Chronologisch;

import Fussball.KhwProzedur;
import Fussball.Spielprozedurorder;
import Meldung.Wertangabefehler;

/**
 * Sucht alle Kalenderhalbwochen nach einer bestimmten {@link Khw Kalenderhalbwoche} (Khw) durch. Falls das Gesuchte existiert, ist sie dem gleich, falls nicht, wird der nächstgrößte Khw ausgewählt
 * und falls es so eine ebenfalls nicht gibt, wird die letzte vorhandene und somit die höchste vorhandene Khw ausgewählt.
 * Passend zur Khw sind die Spiele hier unter {@link #gefundenes()} abrufbar.
 * @author Attila Kiss
 */
public class KhwSucher extends Khw implements KhwProzedur {

	private KhwSpiele gefundenes;
	private Khw aktuelleKhw = new Khw();

	public KhwSucher (Khw gesuchteKhw) throws Wertangabefehler {
		super (gesuchteKhw.zahl, gesuchteKhw.monatszahl, gesuchteKhw.jahreszahl);
	}
	
	/** Liefert die durch den KhwSuche gefundenen {@link KhwSpiele Spiele} */
	public KhwSpiele gefundenes() { return gefundenes; }
	
	public Spielprozedurorder benutzt (KhwSpiele khwSpiele) throws Wertangabefehler {
		aktuelleKhw.zuweisen(khwSpiele);
		gefundenes = khwSpiele;
		int khwVergleich = aktuelleKhw.compareTo(this);
		if (khwVergleich >= 0) {
			return Spielprozedurorder.ENDE;
		} else {
			if (aktuelleKhw.jahreszahl< this.jahreszahl)
				return Spielprozedurorder.NÄCHSTES_JAHR;
			else if (aktuelleKhw.monatszahl< this.monatszahl)
				return Spielprozedurorder.NÄCHSTER_MONAT;
			return Spielprozedurorder.NÄCHSTE_KHW;
		}
	}
	
	public void ende() {}

}
