package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;



import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
 


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

public class Marshalling {

	private MCProblem mcp;

	public  Marshalling(MCProblem mcp) {
		Objects.requireNonNull(mcp);
		this.mcp = mcp;
	}
	
	//set une X2Alternative avec les valeurs d'une alternative
	public X2Alternative x2Alt(ObjectFactory f, Alternative a) { final X2Alternative alt = f.createX2Alternative();
		alt.setId("a" + a.getId());
		return alt;
	}
	
	//set une X2Criterion avec les valeurs d'un critere 
	public X2Criterion x2Crit(ObjectFactory f, Criterion c) {
		final X2Criterion crit = f.createX2Criterion();
		crit.setId("c" + c.getId());
		return crit;

	}

	 
	/**
	 * @param f
	 * @param alt
	 * @return
	 * //set la valeur de perfomance
	 */
	public X2Value x2Val(ObjectFactory f, Alternative alt) {
		final X2Value value = f.createX2Value();
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
	 * @return
	 * configuration  d'une performance   
	 */
	public X2AlternativeOnCriteriaPerformances perftest(ObjectFactory f,
			Alternative a) {
		final X2AlternativeOnCriteriaPerformances.Performance performance = f
				.createX2AlternativeOnCriteriaPerformancesPerformance();
		final X2Value value = f.createX2Value();
		for (Criterion c : mcp.getValueList(a).keySet())
			performance.setCriterionID("c" + c.getId());
		for (Float val : mcp.getValueList(a).values()) {
			value.setReal(val);
			performance.setValue(value);
		}
		return addToPerf(performance, f, a);
	}

	/**
	 * @param performance
	 * @param f
	 * @param a
	 * @return
	 * ajout d'une configuration
	 */
	public X2AlternativeOnCriteriaPerformances addToPerf(
			X2AlternativeOnCriteriaPerformances.Performance performance,
			ObjectFactory f, Alternative a) {
		final X2AlternativeOnCriteriaPerformances performances = f
				.createX2AlternativeOnCriteriaPerformances();
		performances.getPerformance().add(performance);
		performances.setAlternativeID("a" + a.getId());
		return performances;
	}

	/**
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * ecrit dans un fichier XML le McProblem
	 */
	public void marshalAndWrite() throws JAXBException, FileNotFoundException,
			IOException {
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Marshaller marshaller = jc.createMarshaller();
		final ObjectFactory f = new ObjectFactory();

		final X2PerformanceTable perfTable = f.createX2PerformanceTable();

		final X2Alternatives alternatives = f.createX2Alternatives();
		UnmodifiableIterator<Alternative> it = mcp.getAlternatives().iterator();

		//Ajout des alternatives
		while (it.hasNext()) {
			Alternative a = it.next();
			alternatives.getDescriptionOrAlternative().add(x2Alt(f, a));
		}

		final X2Criteria criteria = f.createX2Criteria();
		UnmodifiableIterator<Criterion> itc = mcp.getCriteria().iterator();

		// ajout des criterion 
		while (itc.hasNext()) {
			criteria.getCriterion().add(x2Crit(f, itc.next()));
		}
		
		// ajout Performances dans la table performance
		for (Alternative a : mcp.getAlternatives()) {

			perfTable.getAlternativePerformances().add(perftest(f, a));
		}

		final XMCDA xmcda = f.createXMCDA();
		final List<JAXBElement<?>> xmcdaSubElements = xmcda
				.getProjectReferenceOrMethodMessagesOrMethodParameters();
		xmcdaSubElements.add(f.createXMCDAAlternatives(alternatives));
		xmcdaSubElements.add(f.createXMCDACriteria(criteria));
		xmcdaSubElements.add(f.createXMCDAPerformanceTable(perfTable));

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		//Ecriture dans file1
		try (final FileOutputStream fos = new FileOutputStream(
				new File(
						"resources/resourcesfile1/file1.xml"))) {
			marshaller.marshal(xmcda, fos);
		}

	}


}