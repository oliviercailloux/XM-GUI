package io.github.oliviercailloux.y2018.xmgui.file1;

import java.util.HashSet;
import java.util.Set;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternative;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criterion;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

public class MarshalFunction {

	protected static Set<ObjectFactory> marshallmap = new HashSet<ObjectFactory>();

	// set X2Alternative with Alternative value
	public static X2Alternative x2Alt(Alternative a) {

		final X2Alternative alt = getObjectFactoy().createX2Alternative();
		alt.setId("a" + a.getId());
		return alt;
	}

	// set X2Criterion value with Criterion
	public static X2Criterion x2Crit(Criterion c) {
		final X2Criterion crit = getObjectFactoy().createX2Criterion();
		crit.setId("c" + c.getId());
		return crit;

	}

	/**
	 * @param f
	 * @param alt
	 * @return set Perfomance value
	 */
	public static X2Value x2Val(MCProblem mcp, Alternative alt) {
		final X2Value value = getObjectFactoy().createX2Value();
		UnmodifiableIterator<Float> val = mcp.getValueList(alt).values()
				.iterator();
		while (val.hasNext()) {
			value.setReal(val.next());
			return value;
		}
		return value;
	}

	/**
	 * @param f
	 * @param a
	 * @return Set X2AlternativeOnCriteriaPerformances.Performance
	 */
	public static X2AlternativeOnCriteriaPerformances perftest(MCProblem mcp,
			Alternative a) {
		final X2AlternativeOnCriteriaPerformances.Performance performance = getObjectFactoy()
				.createX2AlternativeOnCriteriaPerformancesPerformance();
		final X2Value value = getObjectFactoy().createX2Value();
		for (Criterion c : mcp.getValueList(a).keySet())
			performance.setCriterionID("c" + c.getId());
		for (Float val : mcp.getValueList(a).values()) {
			value.setReal(val);
			performance.setValue(value);
		}
		return addToPerf(performance, a);
	}

	/**
	 * @param performance
	 * @param f
	 * @param a
	 * @return Performance list Add performance to the list of performance
	 */
	public static X2AlternativeOnCriteriaPerformances addToPerf(
			X2AlternativeOnCriteriaPerformances.Performance performance,
			Alternative a) {
		final X2AlternativeOnCriteriaPerformances performances = getObjectFactoy()
				.createX2AlternativeOnCriteriaPerformances();
		performances.getPerformance().add(performance);
		performances.setAlternativeID("a" + a.getId());
		return performances;
	}

	public static ObjectFactory getObjectFactoy() {
		ObjectFactory of = null;
		for (ObjectFactory f : ImmutableSet.copyOf(marshallmap))
			of = f;
		return of;
	}
}
