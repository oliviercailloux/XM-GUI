package io.github.oliviercailloux.y2018.xmgui.file1;

import java.util.Map;
import java.util.Map.Entry;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances.Performance;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

public class CreatePerformance{

	/**
	 * This method enters creates the X2Performance for a given Criterion object, identified by its unique ID.
	 * 
	 * @param entry the Criterion-Performance value pair
	 * @return performance the X2Performance value for a Criterion object
	 */
	public static X2AlternativeOnCriteriaPerformances.Performance createPerformance(Entry<Criterion, Float> entry) {
		final X2AlternativeOnCriteriaPerformances.Performance performance = MCProblemMarshaller.f.createX2AlternativeOnCriteriaPerformancesPerformance();
		final X2Value value = MCProblemMarshaller.f.createX2Value();
		performance.setCriterionID(BasicObjectsMarshallerToX2.basicCriterionToX2(entry.getKey()).getId());
		value.setReal(entry.getValue());
		performance.setValue(value);
		
		return performance;
	}

}
