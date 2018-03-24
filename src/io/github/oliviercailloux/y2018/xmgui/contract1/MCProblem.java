package io.github.oliviercailloux.y2018.xmgui.contract1;

import java.util.Set;
import java.util.HashSet;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * An object of this class MCProblem contains a set of alternatives, criteria and
 * an evaluation table in order to store a complete multi-criteria problem for decision making.
<<<<<<< HEAD
 *
=======
>>>>>>> 5111ae5dd0b995108069a07b9c1691ccb81ff72f
 */
public class MCProblem {

	// Create a set of Alternative objects
	private Set <Alternative> alternatives = new HashSet <Alternative> ();
	
	// Create a set of Criterion objects
	private Set <Criterion> criteria = new HashSet <Criterion> ();
	
	// Create a Guava Table of Alternative and Criterion objects, 
	// each pair being associated with a value (double)
	private Table<Alternative, Criterion, Double> tableEval = HashBasedTable.create();
		
	/**
	 * Add a value for an alternative-criterion pair
	 * @param alt, c and val must not be null
	 */
	public void putValue(Alternative alt, Criterion c, double val) {
		if (alt == null) {
			throw new NullPointerException("alt can't be null");
		}
		if (c == null) {
			throw new NullPointerException("c can't be null");
		}
		this.alternatives.add(alt);
		this.criteria.add(c);
		tableEval.put(alt, c, val);	
		}
	
	/**
	 * Add an alternative to the Set
	 * @param alt must not be null
	 */
	public void addAlt(Alternative alt) {
		if (alt == null) {
			throw new NullPointerException("alt can't be null");
		}
		this.alternatives.add(alt);
	}
	
	/**
	 * Add a criterion to the Set
	 * @param c must not be null
	 */
	public void addAlt(Criterion c) {
		if (c == null) {
			throw new NullPointerException("c can't be null");
		}
		this.criteria.add(c);
	}
	
<<<<<<< HEAD

=======
	/**
	 * Accessor for an alternative-criterion pair value in the tableEval
	 * @param alt and c must not be null and must be present in the tableEval
	 */
	public double getValue(Alternative alt, Criterion c) {
		if (alt == null) {
			throw new NullPointerException("alt can't be null");
		}
		if (c == null) {
			throw new NullPointerException("c can't be null");
		}
		if (!tableEval.contains(alt, c)) {
			throw new IllegalArgumentException ("the alt-c pair does not exist in the tableEval");
		}
		return tableEval.get(alt, c);
	}

	/**
	 * Accessor for the whole tableEval
	 * @return a copy of the tableEval
	 */
	public Table<Alternative, Criterion, Double> getTableEval() {
		Table<Alternative, Criterion, Double> tempTableEval = tableEval; 
		return tempTableEval;
	}
	
>>>>>>> 5111ae5dd0b995108069a07b9c1691ccb81ff72f
}
