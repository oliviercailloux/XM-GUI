package io.github.oliviercailloux.y2018.xmgui.contract1;

/**
 * An object of this immutable class Criterion contains a simple identifier of type int
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
		
	/** 
	 * Overriding equals() to compare to Criterion objects
	 * according to their ids.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Criterion) {
			Criterion c = (Criterion) o;
			return (this.id == c.id);
		}
		return false;
	}
	
	/** 
	 * Overriding hashCode() because we
	 * overrid equals()
	 */
	@Override
	public int hashCode() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return "CriterionId:<"+ getId()+">";
	}
}
