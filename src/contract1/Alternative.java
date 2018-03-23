package contract1;

/**
<<<<<<< HEAD
 * An object of this immuable class Alternative contains a simple identifier of type integer
 *
=======
 * An object of this immutable class Alternative contains a simple identifier of type int
>>>>>>> 5111ae5dd0b995108069a07b9c1691ccb81ff72f
 */
public final class Alternative {

	private final int id;
	
	/**
	 * Set the alternative's id when constructing it
	 * @param the ID
	 */
	public Alternative(int i) {
		this.id = i;
	}
	
	/**
	 * Accessor function
	 * @return an int
	 */
	public int getId() {
		return id;
	}
}
