package Fussball.Spielobjekte;

import Meldung.Wertangabefehler;

/**
	 * Enthält ein abgebrochenes Spiel
	 * @author Attila Kiss
	 */
	public class AbgebrochenesSpiel extends SpielMitErgebnis implements NeuZuSetzendesSpiel {
		
		final byte spielminute;
		
		/**
		 * Erzeugt ein abgebrochenes Spiel
		 * @author Attila Kiss
		 */
		public AbgebrochenesSpiel (byte[] terminteile, String heimteam, String auswärtsteam, byte heimtore, byte auswärtstore, byte spielminute, short[] quoten) throws Wertangabefehler {
			super (terminteile, heimteam, auswärtsteam, heimtore, auswärtstore, quoten);
			this.spielminute = spielminute;
		}
		
		public String toString() {
			return super.toString() +" (in der " +spielminute +".Min abgebrochen)"; 
		}
	}