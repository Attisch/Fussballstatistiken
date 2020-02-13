package Allgemein;

/**
 * Ein Paar mit zwei Instanzen möglicherweise verschiedenen Typs.
 * Der Vergleich zweier Paare richtet sich nach dem ersten Instanz vom Typ T.
 * @param <T> ist der Typ der ersten Instanz
 * @param <U> ist der Typ der zweiten Instanz
 * @author Attila Kiss
 */
public class Paar<T extends Comparable<T>, U> implements Comparable<Paar<T,U>>{

	public T instanz1;
	public U instanz2;
	
	public Paar (T instanz1, U instanz2) {
		this.instanz1 = instanz1;
		this.instanz2 = instanz2;
	}

	public int compareTo (Paar<T,U> ander) {
		return instanz1.compareTo (ander.instanz1);
	}

}
