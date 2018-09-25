package io.github.oliviercailloux.y2018.xmgui.file1;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternative;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;

/*
 * This class is used to marshal Alternative and Criterion objects to X2Alternative and X2Criterion objects.
 */
public class BasicObjectsMarshallerToX2 {

	public static X2Alternative basicAlternativeToX2(Alternative a) {
		final X2Alternative alt = MCProblemMarshaller.f.createX2Alternative();
		alt.setId(Integer.toString(a.getId()));
		return alt;
	}

	public static X2Criterion basicCriterionToX2(Criterion c) {
		final X2Criterion crit = MCProblemMarshaller.f.createX2Criterion();
		crit.setId(Integer.toString(c.getId()));
		return crit;

	}
}
