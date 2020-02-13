package Baumwurzelstruktur;

/**
 * Ein Objekt mit einem Namen und einer Wurzelstruktur die unendlich viele Unterwurzel haben kann
 * @author Attila Kiss
 */
public final class Unterwurzelstruktur implements Comparable<Unterwurzelstruktur>{
	
	public final String name;
	public Wurzelstruktur<Unterwurzelstruktur> unterwurzel;

	public Unterwurzelstruktur(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Unterwurzelstruktur andere) {
		return this.name.compareTo(andere.name);
	}
	
	

}
