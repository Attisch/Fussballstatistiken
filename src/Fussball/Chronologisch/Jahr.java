package Fussball.Chronologisch;

/**
 * Enthält eine Liste mit {@link Monat Monaten}
 * @author Attila Kiss
 */
public class Jahr {

	public final byte zahl;
	public final Monat[] monate;
	
	public Jahr (byte zahl, Monat[] monate) {
		this.zahl = zahl;
		this.monate = monate;
	}
	
	public String toString() {
		String text = "";
		for (Monat monat : monate)
			text += monat.toString();
		return text;
	}

}
