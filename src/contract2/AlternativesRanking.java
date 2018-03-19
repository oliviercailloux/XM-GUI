package contract2;

import java.util.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import contract1.Alternative;

/**
 * This class contains all alternatives associated with their rank.
 *
 */
public class AlternativesRanking {

	// Create a multimap to store alternative-rank associations
	private SetMultimap<Integer, Alternative> map = HashMultimap.create();

	/**
	 * Constructor. Put a rank for a given alternative.
	 * 
	 * @param rank
	 * @param alt
	 */
	public AlternativesRanking(int rank, Alternative alt) {
		if (rank <= 0)
			throw new IllegalArgumentException(
					"Un rang doit être superieur à 0");
		map.put(rank, alt);
	}

	/**
	 * Put a rank for a given alternative. Can be used to put ex-aequo
	 * alternatives on the same rank rank must be >= 0
	 * 
	 * @param rank
	 * @param alt
	 */
	public void AddAltRank(int rank, Alternative alt) {
		if (rank <= 0)
			throw new IllegalArgumentException(
					"Un rang doit être superieur à 0");
		map.put(rank, alt);
	}

	/**
	 * Alternative accessor. Return the corresponding alternative(s) for a given
	 * rank.
	 * 
	 * @param rang
	 * @return alternative(s)
	 */
	public Set<Alternative> getAltRank(int rank) {
		if (rank <= 0)
			throw new IllegalArgumentException(
					"Un rang doit être superieur à 0");
		return map.get(rank);
	}

	public String toString() {
		String s = "";
		for (int i = 0; i <= map.size(); i++) {
			for (Alternative alt : map.get(i))
				s += " Rank : " + i + " -> " + " AlternativeId : " + alt.getId() + "\n";
		}

		return s;
	}

}