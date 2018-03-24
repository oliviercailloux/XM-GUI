package io.github.oliviercailloux.y2018.xmgui.contract1;

/**
 * An object of this immutable class Alternative contains a simple identifier of type int
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
