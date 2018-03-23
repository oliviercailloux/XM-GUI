package contract2;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import contract1.Alternative;

/**
 * This class contains all alternatives associated with their rank.
 *
 */
public class AlternativesRanking {

	// Create a multimap to store alternative-rank associations
	private SetMultimap<Integer, Alternative> map = HashMultimap.create();
	private SetMultimap<Integer, String> historique = HashMultimap.create();

	/**
	 * Constructor. Put a rank for a given alternative.
	 * 
	 * @param rank
	 * @param alt
	 */
	public AlternativesRanking(int rank, Alternative alt) {
		if (rank <= 0 || alt == null)
			throw new IllegalArgumentException("Argument non valide");
		historique("put by constructor AlternativeId : " + alt.getId());
		map.put(rank, alt);

	}

	/**
	 * Put a rank for a given alternative. Can be used to put ex-aequo
	 * alternatives on the same rank rank must be >= 0
	 * 
	 * @param rank
	 * @param alt
	 */
	public void putAltRank(int rank, Alternative alt) {
		if (rank <= 0 || alt == null)
			throw new IllegalArgumentException("Agrument non valide");
		valideRank(rank);
		historique("put by putAltRank AlternativeId : " + alt.getId());
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

		if (rank <= 0 || (rank > map.size()))
			throw new IllegalArgumentException("Le rang demandé est inexistant");

		return map.get(rank);
	}

	private void valideRank(int rank) {
		if ((rank - map.size()) > 1)
			throw new IllegalArgumentException("Le rang demandé est invalide");

	}

	public ImmutableSetMultimap<Integer, Alternative> getAltSet() {
		return ImmutableSetMultimap.copyOf(map);
	}

	public void removeAlt(Alternative alt) {
		if (alt == null)
			throw new IllegalArgumentException("L'argument doit être non null");
		for (int i = 0; i <= map.size(); i++) {
			for (Alternative a : map.get(i))
				if (a.getId() == alt.getId())
					map.remove(i, alt);
		}

		historique("remove by removeAlt AlternativeId : " + alt.getId());
	}

	public void historique(String h) {
		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		String s = "[" + sdf + "] : " + h;
		historique.put(historique.size() + 1, s);
	}

	public String getHistorique() {
		String h = "";
		for (String val : historique.values())
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

	public static void main(String[] args) {
		Alternative alt=new Alternative(2);
		AlternativesRanking altr = new AlternativesRanking(1,alt);
		altr.toString();
	}
}