package Allgemein;

import Meldung.Fehler;

/**
 * Eine Prozedur wird in Gang gesetzt, die das Objekt {@link #benutzt} und anschließend einen Rückgabewert zurückgibt.
 * Gegebenenfalls wird nach aller mit verschiedenen Objekten aufgerufenen Benutztungen noch etwas am {@link #ende Ende} ausgeführt.
 * @param <R> ist der Typ des Rückgabewerts der Methode {@link #benutzt}
 * @param <O> ist der Typ des Objekts mit dem {@link #benutzt} wird
 * @author Attila Kiss
 */
public interface Prozedur<R, O> {
	
	/** Wird ausgeführt, wenn die Prozedur nicht mehr aufgerufen werden soll (am Ende) */
	void ende() throws Fehler;
	
	/**
	 * Benutzt das Objekt für etwas
	 * @param objekt
	 * @return eine Zahl
	 * @throws Fehler
	 */
	R benutzt (O objekt) throws Fehler;

}
