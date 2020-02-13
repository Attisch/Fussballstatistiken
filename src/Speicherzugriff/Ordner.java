package Speicherzugriff;

import java.util.ArrayList;

/**
 * Beinhaltet einen Gruppennamen und dessen Namensliste, die wiederum aus Namen besteht
 * @author Attila Kiss
 */
public class Ordner {

	public final String name;
	public ArrayList<Ordner> unterordnerliste;
	
	public Ordner (String name) {
		this.name = name;
		unterordnerliste = new ArrayList<>();
	}
	
	private void pfäde (ArrayList<String> pfad, ArrayList<String> pfäde) {
		if (unterordnerliste.size() !=0) {
			// Pfad mit dem nächsten Ordner ergänzen und in den nächsten Ordner gehen
			pfad.add(name);
			for (short s = 0; s< unterordnerliste.size(); s++) {
				unterordnerliste.get(s).pfäde (pfad, pfäde);
				if (s==unterordnerliste.size()-1)
					pfad.remove(pfad.size()-1);
			}
		} else {
			// Pfad mit dem letzten Ordner ergänzen, Pfad zusammenstellen und in die Liste der Pfäde aufnehmen
			String pfadtext = "";
			for (byte b = 0; b< pfad.size(); b++)
				pfadtext += pfad.get(b) +" → ";
			pfadtext += name;
			pfäde.add(pfadtext);
		}
	}
	
	public String[] pfäde() {
		ArrayList<String> pfad = new ArrayList<>(2);
		ArrayList<String> pfäde = new ArrayList<>();
		pfäde (pfad, pfäde);
		return pfäde.toArray(new String[pfäde.size()]);
	}
	
	public String toString() {
		String text = name;
		if (unterordnerliste.size()==0)
			text += "\n";
		else
			for (Ordner drunter : unterordnerliste)
				text += " → " +drunter.toString();
		return text;
	}

}
