package io.github.oliviercailloux.y2018.xmgui.file1;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

public class CreatePerformances {

	/**
	 * @param f
	 * @param a
	 * @return Create Performance with value and criterion for each Alternative
	 */
	public static X2AlternativeOnCriteriaPerformances createPerformance(MCProblem mcp,
			Alternative a) {
		final X2AlternativeOnCriteriaPerformances.Performance performance = MCProblemMarshaller.f
				.createX2AlternativeOnCriteriaPerformancesPerformance();
		final X2Value value = MCProblemMarshaller.f.createX2Value();
		for (Criterion c : mcp.getValueList(a).keySet())
			performance.setCriterionID("c" + c.getId());
		for (Float val : mcp.getValueList(a).values()) {
			value.setReal(val);
			performance.setValue(value);
		}
		return addPerformanceToPerformances(performance, a);
	}

	/**
	 * @param performance
	 * @param f
	 * @param a
	 * @return  Add performance to Performances
	 */
	public static X2AlternativeOnCriteriaPerformances addPerformanceToPerformances(
			X2AlternativeOnCriteriaPerformances.Performance performance,
			Alternative a) {
		final X2AlternativeOnCriteriaPerformances performances = MCProblemMarshaller.f
				.createX2AlternativeOnCriteriaPerformances();
		performances.getPerformance().add(performance);
		performances.setAlternativeID("a" + a.getId());
		return performances;
	}


}
