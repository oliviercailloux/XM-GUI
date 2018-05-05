package io.github.oliviercailloux.y2018.xmgui.file1;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.UnmodifiableIterator;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternative;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternatives;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criteria;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criterion;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2PerformanceTable;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

public class MCProblemMarshaller {

	private MCProblem mcp;
	
	 
	
	public  MCProblemMarshaller(MCProblem mcp) {
		Objects.requireNonNull(mcp);
		this.mcp = mcp;
	}
	

	/**
	 * @param fos 
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * ecrit dans un fichier XML le McProblem
	 */
	public void marshalAndWrite(FileOutputStream fos) throws JAXBException, FileNotFoundException,
			IOException {
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Marshaller marshaller = jc.createMarshaller();
		final ObjectFactory f = new ObjectFactory();
		BasicObjectsMarshallerToX2.setObjectfactory(f);

		final X2PerformanceTable perfTable = f.createX2PerformanceTable();

		final X2Alternatives alternatives = f.createX2Alternatives();
		UnmodifiableIterator<Alternative> it = mcp.getAlternatives().iterator();

		//Ajout des alternatives
		while (it.hasNext()) {
			Alternative a = it.next();
			alternatives.getDescriptionOrAlternative().add(BasicObjectsMarshallerToX2.x2Alt(a));
		}

		final X2Criteria criteria = f.createX2Criteria();
		UnmodifiableIterator<Criterion> itc = mcp.getCriteria().iterator();

		// ajout des criterion 
		while (itc.hasNext()) {
			criteria.getCriterion().add(BasicObjectsMarshallerToX2.x2Crit(itc.next()));
		}
		
		// ajout Performances dans la table performance
		for (Alternative a : mcp.getAlternatives()) {

			perfTable.getAlternativePerformances().add(BasicObjectsMarshallerToX2.perftest(mcp,a));
		}

		final XMCDA xmcda = f.createXMCDA();
		final List<JAXBElement<?>> xmcdaSubElements = xmcda
				.getProjectReferenceOrMethodMessagesOrMethodParameters();
		xmcdaSubElements.add(f.createXMCDAAlternatives(alternatives));
		xmcdaSubElements.add(f.createXMCDACriteria(criteria));
		xmcdaSubElements.add(f.createXMCDAPerformanceTable(perfTable));

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(xmcda, fos);
		
		
		
		

	}
	
	


}