package io.github.oliviercailloux.y2018.xmgui.contract1;

//import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

import com.google.common.collect.HashBasedTable;
//import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Table;


/**
 * An object of this class MCProblem contains a set of alternatives, criteria
 * and an evaluation table in order to store a complete multi-criteria problem
 * for decision making.
 */
public class MCProblem {

	private Set<Alternative> alternatives = new HashSet<>();
	private Set<Criterion> criteria = new HashSet<>();
	private Table<Alternative, Criterion, Float> tableEval = HashBasedTable.create();

	/**
	 * Add a value for an alternative-criterion pair
	 * 
	 * @param alt must not be null
	 * @param c must not be null
	 * @param val must not be null
	 */
	public void putEvaluation(Alternative alt, Criterion c, Float d) {
		this.alternatives.add(Objects.requireNonNull(alt));
		this.criteria.add(Objects.requireNonNull(c));
		tableEval.put(alt, c, d);
	}

	/**
	 * Add an alternative to the Set
	 * 
	 * @param alt must not be null
	 */
	public void addAlt(Alternative alt) {
		this.alternatives.add(Objects.requireNonNull(alt));
	}

	/**
	 * Add a criterion to the Set
	 * 
	 * @param c must not be null
	 */
	public void addCrit(Criterion c) {
		this.criteria.add(Objects.requireNonNull(c));
	}

	/**
	 * Accessor for an alternative-criterion pair value in the tableEval
	 * 
	 * @param alt and c must not be null and must be present in the tableEval
	 */
	public double getValue(Alternative alt, Criterion c) {
		if (!tableEval.contains(Objects.requireNonNull(alt), Objects.requireNonNull(c))) {
			throw new IllegalArgumentException("the alt-c pair does not exist in the tableEval");
		}
		return tableEval.get(Objects.requireNonNull(alt), Objects.requireNonNull(c));
	}

	/**
	 * Accessor for the whole tableEval
	 * 
	 * @return an immutable copy of the tableEval
	 */
	public ImmutableTable<Alternative, Criterion, Float> getTableEval() {
		return ImmutableTable.copyOf(tableEval);
	}
	
	public ImmutableSet<Alternative> getAlternatives(){
		return ImmutableSet.copyOf(alternatives);
	}
	
	public ImmutableSet<Criterion> getCriteria(){
		return ImmutableSet.copyOf(criteria);
	}
	
	public ImmutableMap<Criterion,Float> getValueList(Alternative alt){
		return ImmutableMap.copyOf(tableEval.row(alt));
	}
	
	public String toStringTableEval(){
		String s="---------------------------TableEval--------------------------------------\n";
		for (Alternative key : tableEval.rowKeySet()) {
	        s+=key.toString()+"\n";
	        for (java.util.Map.Entry<Criterion, Float> row : tableEval.row(key).entrySet()) {
	            s+="\t"+ row.getKey().toString() + " value : " + row.getValue() +"\n";
	        }
	    }
		s+="\n---------------------------TableEval--------------------------------------";
		return s;
	}
}
