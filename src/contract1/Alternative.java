package contract1;

/**
 * An object of this immuable class Alternative contains a simple identifier of type integer
 * @author Razorin
 *
 */
public final class Alternative {

	private final int id;
	
	/**
	 * Constructor
	 * @param i is an integer, the ID
	 */
	public Alternative(int i) {
		this.id = i;
	}
	
	/**
	 * Accessor function
	 */
	public int getId() {
		// Return id
		return id;
	}
}
