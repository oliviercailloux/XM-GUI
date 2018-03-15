package contract1;

import java.util.Set;
import java.util.HashSet;

/**
 * An object of this class MCProblem contains a set of alternatives, criteria and
 * an evaluation table in order to store a complete multi-criteria problem for decision making.
 * @author Razorin
 *
 */
public class MCProblem {

	// Create a set of Alternative objects
	Set <Alternative> Alternatives = new HashSet <Alternative> ();
	
	// Create a set of Criterion objects
	Set <Criterion> Criteria = new HashSet <Criterion> ();
	
}
