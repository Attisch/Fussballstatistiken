package Speicherzugriff;

import Allgemein.Prozedur;

/**
 * Diese {@link Prozedur} wird beim Laden von Dateien verwendet, dabei wird jede eingelesene Zeile der {@link #datei Datei} für etwas benutzt.
 * Abbruch der Einlesung bei "false" als Rückgabewert. Am Ende der Ladeprozedere stehen die erzeugten Daten im Objekt bereit.
 * @author Attila Kiss
 */
public interface Ladeprozedur extends Prozedur<Boolean, String> {
	
	/** @return Dateiname mit Pfad */
	String datei();
	
}
