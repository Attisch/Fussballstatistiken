package Visualität;

/**
 * Beinhaltet alle Ausgabemöglichkeiten auf der Console, die einmalig geladen werden muss.
 * @author Attila Kiss
 *
 */
public final class Console extends Ausgabefähig {
	
	private static Console console;
	
	private Console() {}
	
	public static Console ladeConsole() {
		if (console==null)
			console = new Console();
		return console;
	}
			
	/**
	 * Gibt einen Text auf der Console aus und springt automatisch in die nächste Zeile
	 */
	public void text (String text) {
		System.out.println (text);
	}

	/**
	 * Gibt einen Text auf der Console aus und bleibt in der Zeile für den Rest
	 */
	public void textteil (String text) {
		System.out.print (text);
	}
}
