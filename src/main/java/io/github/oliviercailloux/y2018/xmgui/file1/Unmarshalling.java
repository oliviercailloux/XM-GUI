package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternative;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternatives;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criteria;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criterion;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2PerformanceTable;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;


public class Unmarshalling {
	
	private MCProblem mcp = new MCProblem();

	/**
	 * @return
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public MCProblem unmarshalAndStore() throws JAXBException, FileNotFoundException, IOException {
		
		
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		// final ObjectFactory f = new ObjectFactory();
		
		// Il faudra modifier ce fonctionnement car le file doit être dans le file path (cf. mail de Cailloux)
		try (final FileInputStream fis = new FileInputStream( new File("src/test/resources/io/github/oliviercailloux/y2018/xmgui/resourcesfile1/file1.xml"))) {
			
			final XMCDA xmcda = (XMCDA) unmarshaller.unmarshal(fis);
			final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
			
			//////// FOR ALTERNATIVES ///////
			X2Alternatives alts = getX2Alternatives(xmcdaSubElements);
			List<Object> x2AltsList = alts.getDescriptionOrAlternative();
			for (int i = 0; i < x2AltsList.size(); i++) {
				Alternative a = extractAltFromX2Alternatives(i, x2AltsList);
				mcp.addAlt(a);
			}
			
			//////// FOR CRITERIA ///////
			X2Criteria crits = getX2Criteria(xmcdaSubElements);
			List<X2Criterion> critsList = crits.getCriterion();
			for (int i = 0; i < critsList.size(); i++) {
				Criterion c = extractCritFromX2Criteria(i, critsList);
				mcp.addCrit(c);
			}

			//////// FOR PERFORMANCE ///////
			X2PerformanceTable perfTable = getX2PerformanceTable(xmcdaSubElements);
			// Get the list of X2Alternative performances
			List<X2AlternativeOnCriteriaPerformances> altsOnCritsPerf = perfTable.getAlternativePerformances();
			
			for (int i = 0; i < altsOnCritsPerf.size(); i++) {
				getListOfX2AlternativePerformancesOnCriteriaAndPutInMcp(i, altsOnCritsPerf);
			}
			
		} // end try
		
		return mcp;
				
	} // end marshalAndStore
	
	
	public X2Alternatives getX2Alternatives(List<JAXBElement<?>> xmcdaSubElements) {
		// Find the index of the xmcdaSubElements list where there are the X2Alternatives
		int altsIndex = 0;
		while (altsIndex < xmcdaSubElements.size()) {
			if ( xmcdaSubElements.get(altsIndex).getName().toString().equalsIgnoreCase("alternatives") ) {
				break;
			}
			altsIndex++;
		} 
		X2Alternatives alts = (X2Alternatives) xmcdaSubElements.get(altsIndex).getValue();
		return alts;
	}
	
	public Alternative extractAltFromX2Alternatives(int i, List<Object> x2AltsList) {
		X2Alternative x2Alt = (X2Alternative) x2AltsList.get(i);
		String x2AltId = x2Alt.getId();
		Alternative a = new Alternative(Integer.parseInt(x2AltId.substring(1)));
		return a;
	}
	
	public X2Criteria getX2Criteria(List<JAXBElement<?>> xmcdaSubElements) {
		// Find the index of the xmcdaSubElements list where there are the X2Criteria
		int critsIndex = 0;
		while (critsIndex < xmcdaSubElements.size()) {
			if ( xmcdaSubElements.get(critsIndex).getName().toString().equalsIgnoreCase("criteria") ) {
				break;
				}
			critsIndex++;
			} 
		X2Criteria crits = (X2Criteria) xmcdaSubElements.get(critsIndex).getValue();
		return crits;
	}
	
	public Criterion extractCritFromX2Criteria(int i, List<X2Criterion> x2CritsList) {
		X2Criterion x2Crit = x2CritsList.get(i);
		String x2CritId = x2Crit.getId();
		Criterion c = new Criterion(Integer.parseInt(x2CritId.substring(1)));		
		return c;
	}
	
	public X2PerformanceTable getX2PerformanceTable(List<JAXBElement<?>> xmcdaSubElements) {
		// Find the index of the xmcdaSubElements list where there is the performances table
		int perfsIndex = 0;
		while (perfsIndex < xmcdaSubElements.size()) {
			if ( xmcdaSubElements.get(perfsIndex).getName().toString().equalsIgnoreCase("performanceTable") ) {
				break;
				}
			perfsIndex++;
			} 
		// Visual verification on console		
		// System.out.println(xmcdaSubElements.get(perfsIndex).getName());
		// Get X2PerformanceTable
		X2PerformanceTable perfTable = (X2PerformanceTable) xmcdaSubElements.get(perfsIndex).getValue();		
		return perfTable;
	}
	
	public void getListOfX2AlternativePerformancesOnCriteriaAndPutInMcp(int i, List<X2AlternativeOnCriteriaPerformances> altsOnCritsPerf) {
		X2AlternativeOnCriteriaPerformances firstEvalAlt = altsOnCritsPerf.get(i);
		// Get the id of the first evaluated X2Alternative
		String firstEvalAltId = firstEvalAlt.getAlternativeID();
		Alternative a = new Alternative(Integer.parseInt(firstEvalAltId.substring(1)));
		List<X2AlternativeOnCriteriaPerformances.Performance> EvaluatedAltPerfs = firstEvalAlt.getPerformance();		

		for (int j = 0; j < EvaluatedAltPerfs.size(); j++) {
			X2AlternativeOnCriteriaPerformances.Performance firstEvalAltPerfOnCrit = EvaluatedAltPerfs.get(j);
			String firstEvalAltPerfOnCritId = firstEvalAltPerfOnCrit.getCriterionID();
			X2Value firstEvalAltPerfOnCritIdValue = firstEvalAltPerfOnCrit.getValue();
			// Put the Alternative-Criteria pair in the tableEval with value of performance
			Criterion c = new Criterion(Integer.parseInt(firstEvalAltPerfOnCritId.substring(1)));
			double v = firstEvalAltPerfOnCritIdValue.getReal();
			mcp.putValue(a, c, (float) v);
		}
		
	}
}
