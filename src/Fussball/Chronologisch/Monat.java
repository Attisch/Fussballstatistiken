package Fussball.Chronologisch;

/**
 * Enthält eine Liste mit {@link KhwSpiele Kalenderhalbwochen}
 * @author Attila Kiss
 */
public class Monat {

	public final byte zahl;
	public final KhwSpiele[] khwSpiele;
	
	public Monat (byte zahl, KhwSpiele[] spiele) {
		this.zahl = zahl;
		this.khwSpiele = spiele;
	}
	
	public String toString() {
		String text = "";
		for (KhwSpiele khw : khwSpiele)
			text += khw.toString();
		return text;
	}
}
