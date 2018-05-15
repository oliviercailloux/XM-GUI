package io.github.oliviercailloux.y2018.xmgui.contract1;

import java.util.Objects;

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
	
	/** 
	 * Overriding equals() to compare Alternative objects according to their id.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Alternative)) {
			return false;
		}
		Alternative a = (Alternative) o;
		return (this.id == a.id);
	}
	
	/** 
	 * Overriding hashCode() because we
	 * overrid equals()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public String toString(){
		return "AlternativeId:<"+ getId()+">";
	}
}
