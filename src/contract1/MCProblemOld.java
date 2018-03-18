package contract1;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Iterator;

/**
 * An object of this class MCProblem contains a set of alternatives, criteria and
 * an evaluation table in order to store a complete multi-criteria problem for decision making.
 * @author Razorin
 *
 */
public class MCProblemOld {

	// Create a set of Alternative objects
	protected Set <Alternative> alternatives = new HashSet <Alternative> ();
	
	// Create a set of Criterion objects
	protected Set <Criterion> criteria = new HashSet <Criterion> ();
	
	// Create a Guava Table of Alternatives and Criterion objects, 
	// each pair being associated with a value (double)
	protected Table<Alternative, Criterion, Double> tableEval = HashBasedTable.create();
	
	/**
	 * Constructor. 
	 * Put all the Alternatives and Criteria sets' elements into the table.
	 */
	public MCProblemOld() {
		// Create iterators to navigate the alternatives and criteria sets
		Iterator<Alternative> altItr = alternatives.iterator();
		Iterator<Criterion> critItr = criteria.iterator();
		// Iterate over alternatives set elements
		while (altItr.hasNext()) {
			Alternative alt = altItr.next();
			// For each alternative, put each criterion
			while (critItr.hasNext())
				tableEval.put(alt, critItr.next(), (double) 0);
		}
	}
	
	/**
	 * Add alternatives objects to the Alternatives Set
	 */
	public void addAlt(Alternative alt) {
		this.alternatives.add(alt);
	}
	
	/**
	 * Add criterion objects to the Criteria Set
	 */
	public void addCrit(Criterion c) {
		this.criteria.add(c);
	}
	
	/**
	 * Add value for an alternative-criterion pair
	 */
	public void addValue(Alternative alt, Criterion c, Double val) {
		tableEval.put(alt, c, val);	
		}
	
	/**
	 * TableEval accessor for an alternative-criterion pair value
	 */
	public Double getCriteriaValues(Alternative alt, Criterion c) {
		return tableEval.get(alt, c);
	}
	
	/**
	 * TableEval accessor to return the values of a specific alternative for all the criteria in the set
	 */
	public Map<Criterion, Double> getCriteriaValues(Alternative alt) {
		return tableEval.row(alt);
	}
	
	/**
	 * TableEval accessor to return the values of a specific criterion for all the alternatives in the set
	 */
	public Map<Alternative, Double> getAlternativesValues(Criterion c) {
		return tableEval.row(c);
	}
}
