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
 *
 */
public class MCProblem {

	// Create a set of Alternative objects
	protected Set <Alternative> alternatives = new HashSet <Alternative> ();
	
	// Create a set of Criterion objects
	protected Set <Criterion> criteria = new HashSet <Criterion> ();
	
	// Create a Guava Table of Alternatives and Criterion objects, 
	// each pair being associated with a value (double)
	protected Table<Alternative, Criterion, Double> tableEval = HashBasedTable.create();
	
	/**
	 * Constructor. 
	 */
	public MCProblem(Alternative alt, Criterion c, Double val){
		this.alternatives.add(alt);
		this.criteria.add(c);
		tableEval.put(alt, c,val);
	}
	
	/**
	 * Add value for an alternative-criterion pair
	 */
	public void addValue(Alternative alt, Criterion c, Double val) {
		if (!alternatives.contains(alt))
			this.alternatives.add(alt);
		if (!criteria.contains(c))
			this.criteria.add(c);
		tableEval.put(alt, c, val);	
		}
	
	/**
	 * TableEval accessor for an alternative-criterion pair value
	 */
	public Double getCriteriaValues(Alternative alt, Criterion c) {
		return tableEval.get(alt, c);
	}
	

}
