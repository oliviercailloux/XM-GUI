package contract2;

import java.util.Collection;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import contract1.Alternative;

/**
 * This class contains all alternatives associated with their rank.
 * @author Razorin
 *
 */
public class AlternativesRanking {
	
	// Create a multimap to store alternative-rank associations
	private SetMultimap<Integer, Alternative> map = HashMultimap.create();

	/**
	 * Constructor. 
	 * Put a rank for a given alternative.
	 * @param rank
	 * @param alt
	 */
	public AlternativesRanking(int rank, Alternative alt) {
		map.put(rank, alt);
	}
	
	/**
	 * Put a rank for a given alternative.
	 * Can be used to put ex-aequo alternatives on the same rank
	 * @param rank
	 * @param alt
	 */
	public void AddAltRank(int rank, Alternative alt) {
		map.put(rank, alt);
	}

	/**
	 * Alternative accessor. Return the corresponding alternative(s) for a given rank.
	 * @param rang
	 * @return alternative(s)
	 */
	public Collection<Alternative> getAltRank(int rank) {
		return map.get(rank);
	}
	

}