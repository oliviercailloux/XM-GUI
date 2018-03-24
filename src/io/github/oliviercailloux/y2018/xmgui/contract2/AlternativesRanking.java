package io.github.oliviercailloux.y2018.xmgui.contract2;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;

/**
 * This class contains all alternatives associated with their rank.
 */
public class AlternativesRanking {

	// Create a multimap to store alternative-rank associations
	private SetMultimap<Integer, Alternative> map = HashMultimap.create();
	// Create a multimap to store an history of operations performed on map
	private SetMultimap<Integer, String> history = HashMultimap.create();

	/**
	 * Put a rank for a given alternative in order to construct and AlternativeRanking instance
	 * This also creates a history entry for the put alternative.
	 * 
	 * @param rank must be positive
	 * @param alt must not be null
	 */
	public AlternativesRanking(int rank, Alternative alt) {
		if (rank <= 0 || alt == null)
			throw new IllegalArgumentException("rank must be positive and alt must not be null");
		// Create an history for this AlternativesRanking instance
		history("Inserted by constructor AlternativeId : " + alt.getId());
		map.put(rank, alt);

	}

	/**
	 * Put a rank for a given alternative. Can be used to put ex-aequo alternatives on the same rank.
	 * This also creates a history entry for the put alternative.
	 * 
	 * @param rank must be positive and inferior to the total number of alternatives + 1
	 * @param alt must not be null
	 */
	public void putAltRank(int rank, Alternative alt) {
		if (rank <= 0 || alt == null)
			throw new IllegalArgumentException("rank must be positive and alt must not be null");
		// Validate the rank given is not too big
		validateRank(rank);
		history("Inserted by putAltRank AlternativeId : " + alt.getId());
		map.put(rank, alt);
	}

	/**
	 * Alternative accessor. Return the corresponding alternative(s) for a given
	 * rank.
	 * 
	 * @param rank must be positive and inferior to the total number of alternatives + 1
	 * @return alternative(s)
	 */
	public Set<Alternative> getAlt(int rank) {

		if (rank <= 0 || (rank > map.size() + 1))
			throw new IllegalArgumentException("Given rank does not exist");

		return map.get(rank);
	}

	/**
	 * Accessor to get a copy of the AlternativesRanking map.
	 */
	public ImmutableSetMultimap<Integer, Alternative> getAltSet() {
		return ImmutableSetMultimap.copyOf(map);
	}

	/**
	 * Remove all entries of a given alternative from the map.
	 * This also creates a history entry for the removed alternative.
	 * 
	 * @param alt must not be null
	 */
	public void removeAlt(Alternative alt) {
		if (alt == null)
			throw new IllegalArgumentException("alt must not be null");
		for (int i: map.keys()) {
			for (Alternative a : map.get(i))
				if (a.getId() == alt.getId()) {
					map.remove(i, alt);
				}
		}
		history("Remove by removeAlt AlternativeId : " + alt.getId());
	}
	
	/**
	 * Validate that the rank about to be put is inferior to the number of alternatives + 1.
	 */
	private void validateRank(int rank) {
		if ( (rank > map.size() +1) ) {
			throw new IllegalArgumentException("Given rank is invalid (too big).");
		}
	}
	
	/**
	 * Add a timestamped history entry.
	 */
	public void history(String h) {
		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		String s = "[" + sdf + "] : " + h;
		history.put(history.size() + 1, s);
	}

	public String getHistory() {
		String h = "";
		for (String val : history.values())
			h += val + "\n";
		h += "[ Map size " + map.size() + " ]";
		return h;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i <= map.size(); i++) {
			for (Alternative alt : map.get(i))
				s += " Rank : " + i + " -> " + " AlternativeId : "
						+ alt.getId() + "\n";
		}
		return s;
	}
	
}