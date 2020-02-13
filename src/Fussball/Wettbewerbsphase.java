package Fussball;

/**
 * Enthält die Phasenangabe der Spiele, d.h. Interpretationsmöglichkeiten der Ergebnisse werden eingeschränkt.
 * Mögliche Phasen sind: UNBEKANNT, ENTSCHEIDUNGSSPIELPHASE, HINSPIELPHASE, RÜCKSPIELPHASE, LIGASPIELPHASE, GRUPPENPHASE
 * @author Attila Kiss
 */
public enum Wettbewerbsphase {
	UNBEKANNT, ENTSCHEIDUNGSSPIELPHASE, HINSPIELPHASE, RÜCKSPIELPHASE, LIGASPIELPHASE, GRUPPENPHASE;
	
	public String toString() {
		return Allgemein.Verwendbare.zuNomen(super.toString());
	}
}
