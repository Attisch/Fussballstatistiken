package Fussball;

/**
 * @author Attila Kiss
 */
public class Gruppentabelle extends Tabelle {
	
	public final char gruppenbezeichner;

	public Gruppentabelle (byte maxPlätze, char bezeichner) {
		super (maxPlätze);
		gruppenbezeichner = bezeichner;
	}

}
