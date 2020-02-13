package Baumwurzelstruktur;

import Baumwurzelstruktur.Wurzelknoten;

/**
 * Enthält einen Wurzelknoten mit weiteren möglichen Knoten links und rechts. Jeder Knoten hat einen Inhalt des Typen I
 * @param <I> ist der Typ des Inhalts
 * @author Attila Kiss
 */
final class Wurzelknoten<I extends Comparable<I>>{

	private final I inhalt;
	private Wurzelknoten<I> linkerKnoten;
	private Wurzelknoten<I> rechterKnoten;

	/**
	 * Erzeugt einen Knoten mit Inhalt
	 * @param inhalt
	 */
	public Wurzelknoten(I inhalt) {
		this.inhalt = inhalt;
	}
	
	/**
	 * @param inhalt nach dem gesucht wird
	 * @return null wenn kein Element mit dem angegebenen Inhalt gefunden wurde, ansonsten das Element selbst
	 */
	public final I gefunden (I inhalt) {
		if (this.inhalt.compareTo(inhalt)< 0)
			if (rechterKnoten!=null)
				return rechterKnoten.gefunden(inhalt);
			else return null;
		else if (this.inhalt.compareTo(inhalt) >0)
			if (linkerKnoten!=null)
				return linkerKnoten.gefunden(inhalt);
			else return null;
		else
			return this.inhalt;
	}
	
	/**
	 * Fügt den Inhalt im Wurzelknoten ein und geht zum nächsten Knoten
	 * @param inhalt zum einfügen
	 */
	public final boolean verwurzel (I inhalt) {
		if (this.inhalt.compareTo(inhalt)< 0)						// Wenn Inhalt größer als Knoteninhalt
			if (rechterKnoten == null) {							// gehe nach rechts
				rechterKnoten = new Wurzelknoten<I>(inhalt);
				return true;
			} else return rechterKnoten.verwurzel(inhalt);
		else if (this.inhalt.compareTo(inhalt) >0)					// Wenn Inhalt kleiner als Knoteninhalt
			if (linkerKnoten == null) {								// gehe nach links
				linkerKnoten = new Wurzelknoten<I>(inhalt);
				return true;
			}
			else return linkerKnoten.verwurzel(inhalt);
		else return false;
	}
	
	/**
	 * Der Inhalt dieses Wurzelkontens wird in die angegebene Wurzel integriert (aufgenommen) und es geht mit dem nächsten Knoten weiter
	 * @param wurzel der neu ergänzt wird
	 */
	public final void integriereIn (Wurzelstruktur<I> wurzel) {
		wurzel.verwurzel(inhalt);
		if (linkerKnoten!=null)
			linkerKnoten.integriereIn(wurzel);
		if (rechterKnoten!=null)
			rechterKnoten.integriereIn(wurzel);
	}
	
	/**
	 * @param text der durch jedes Element weitergereicht und ergänzt wird
	 * @param trennzeichen zwischen den Elementen
	 * @return geordnete Auflistung von kleinster bis größter Inhalt
	 */
	public final String geordneterText (String text, String trennzeichen) {
		if (linkerKnoten !=null)
			text = linkerKnoten.geordneterText (text, trennzeichen);
		text += inhalt.toString() +trennzeichen;
		if (rechterKnoten !=null)
			text = rechterKnoten.geordneterText (text, trennzeichen);
		return text;			
	}
	
	public String toString() {
		return inhalt.toString();
	}
	
	
}