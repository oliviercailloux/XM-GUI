package contract1;

/**
<<<<<<< HEAD
 * An object of this immuable class Criterion contains a simple identifier of type integer
 *
=======
 * An object of this immutable class Criterion contains a simple identifier of type int
>>>>>>> 5111ae5dd0b995108069a07b9c1691ccb81ff72f
 */
public final class Criterion {

	private final int id;
	
	/**
	 * Set the criterion's id when constructing it
	 * @param an int
	 */
	public Criterion(int i) {
		this.id = i;
	}
	
	/**
	 * Accessor function
	 */
	public int getId() {
		return id;
	}
}
