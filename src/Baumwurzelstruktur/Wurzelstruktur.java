package Baumwurzelstruktur;

/**
 * Enthält eine Wurzelstruktur mit einem Wurzelknoten, der wiederrum links oder rechts mit weiteren Wurzelknoten erweitert werden kann.
 * Jeder Wurzelknoten hat einen Inhalt des Typs I. Bei jedem Knoten kommen links die kleineren Werte des Inhalts, nach rechts die größeren als der aktuelle Inhalt des Knotens ist.
 * @param <I> Typ des Inhalts
 * @author Attila Kiss
 */
public final class Wurzelstruktur<I extends Comparable<I>>{

	private Wurzelknoten<I> wurzel;
	private int anzahl = 0;
	
	/**
	 * @return Anzahl der Wurzelknoten (Objekte)
	 */
	public final int anzahl() {
		return anzahl;
	}
	
	/**
	 * Zurück zum Anfang
	 */
	public final void entwurzeln() {
		wurzel = null;
		anzahl = 0;
	}

	/**
	 * @param inhalt nach dem gesucht wird
	 * @return null wenn kein Element mit dem angegebenen Inhalt in der Wurzel gefunden wurde, ansonsten das Element selbst
	 */
	public final I gefunden (I inhalt) {
		if (anzahl==0)
			return null;
		return wurzel.gefunden(inhalt);
	}
	
	public String toString (String trennzeichen) {
		String text;
		if (anzahl!=0)
			return (text = wurzel.geordneterText("", trennzeichen)).substring(0, text.length()-trennzeichen.length());
		return "";
	}
	
	public String toString() {
		return toString (", ");
	}
	
	/**
	 * Fügt den Inhalt im Wurzel ein
	 * @param inhalt zum einfügen
	 */
	public void verwurzel (I inhalt) {
		if (anzahl==0) {
			wurzel = new Wurzelknoten<I>(inhalt);
			anzahl++;
		} else if (wurzel.verwurzel(inhalt))
			anzahl++;
	}
	
	/**
	 * Der gesamte Inhalt dieser Wurzel wird in die angegebene Wurzel integriert
	 * @param wurzel der neu ergänzt wird
	 */
	public void integriereIn (Wurzelstruktur<I> wurzel) {
		if (anzahl!=0) {
			this.wurzel.integriereIn(wurzel);
		}
	}
}