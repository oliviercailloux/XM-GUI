package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternative;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternatives;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativesSet;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criteria;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criterion;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2PerformanceTable;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;


public class Unmarshalling2 {
	
	private MCProblem mcp = new MCProblem();

	public MCProblem unmarshalAndStore() throws JAXBException, FileNotFoundException, IOException {
		
		
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		// final ObjectFactory f = new ObjectFactory();
		
		try (final FileInputStream fis = new FileInputStream( new File("/Users/Razorin/Desktop/PERSO/MIAGE/DEV/JAVA/Test resources/test6realCars.xml"))) {
			
			final XMCDA xmcda = (XMCDA) unmarshaller.unmarshal(fis);
			final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
			System.out.println(xmcdaSubElements);
			
			//////// FOR ALTERNATIVES ///////
			// Find the index of the xmcdaSubElements list where there are the X2Alternatives
			int altsIndex = 0;
			while (altsIndex < xmcdaSubElements.size()) {
				if ( xmcdaSubElements.get(altsIndex).getName().toString().equalsIgnoreCase("alternatives") ) {
				break;
				}
				altsIndex++;
				} 
			// Visual verification on console		
			System.out.println(xmcdaSubElements.get(altsIndex).getName());
			// Get X2Alternatives
			X2Alternatives alts = (X2Alternatives) xmcdaSubElements.get(altsIndex).getValue();
			// Drilldown to get List of X2Alternative inside
			List<Object> altsList = alts.getDescriptionOrAlternative();
			for (int i = 0; i < altsList.size(); i++) {
				//Drilldown to get the first X2Alternative
				X2Alternative firstAlt = (X2Alternative) altsList.get(i);
				//Drilldown to get the first X2Alternative's id
				String firstAltId = firstAlt.getId();
				// Put it in an Alternative object
				Alternative a = new Alternative(Integer.parseInt(firstAltId.substring(1)));
				// Put it in an MCProblem object
				mcp.addAlt(a);
				
			}
			
			//////// FOR CRITERIA ///////
			// Find the index of the xmcdaSubElements list where there are the X2Criteria
			int critsIndex = 0;
			while (critsIndex < xmcdaSubElements.size()) {
				if ( xmcdaSubElements.get(critsIndex).getName().toString().equalsIgnoreCase("criteria") ) {
					break;
					}
				critsIndex++;
				} 
			// Visual verification on console		
			System.out.println(xmcdaSubElements.get(critsIndex).getName());
			// Get X2Criteria
			X2Criteria crits = (X2Criteria) xmcdaSubElements.get(critsIndex).getValue();
			// Get X2Criterion list
			List<X2Criterion> critsList = crits.getCriterion();
			for (int i = 0; i < critsList.size(); i++) {
				// Get first criterion
				X2Criterion firstCrit = critsList.get(i);
				// Get firstCriterion's id
				String firstCritId = firstCrit.getId();
				// Put it in a Criterion object from contract1
				Criterion c = new Criterion(Integer.parseInt(firstCritId.substring(1)));
				//Put it in an MCProlem object
				mcp.addCrit(c);
			}

			//////// FOR PERFORMANCE ///////
			// Find the index of the xmcdaSubElements list where there is the performances table
			int perfsIndex = 0;
			while (perfsIndex < xmcdaSubElements.size()) {
				if ( xmcdaSubElements.get(perfsIndex).getName().toString().equalsIgnoreCase("performanceTable") ) {
					break;
					}
				perfsIndex++;
				} 
			// Visual verification on console		
			System.out.println(xmcdaSubElements.get(perfsIndex).getName());
			// Get X2PerformanceTable
			X2PerformanceTable perfTable = (X2PerformanceTable) xmcdaSubElements.get(perfsIndex).getValue();
			// Get the list of X2Alternative performances
			List<X2AlternativeOnCriteriaPerformances> altsOnCritsPerf = perfTable.getAlternativePerformances();
			for (int i = 0; i < altsOnCritsPerf.size(); i++) {
				X2AlternativeOnCriteriaPerformances firstEvalAlt = altsOnCritsPerf.get(i);
				// Get the id of the first evaluated X2Alternative
				String firstEvalAltId = firstEvalAlt.getAlternativeID();
				Alternative a = new Alternative(Integer.parseInt(firstEvalAltId.substring(1)));
				List<X2AlternativeOnCriteriaPerformances.Performance> firstEvalAltPerf = firstEvalAlt.getPerformance();
				// Iterate over all the criteria this first X2Alternative was evaluated on
				for (int j = 0; j < firstEvalAltPerf.size(); j++) {
					X2AlternativeOnCriteriaPerformances.Performance firstEvalAltPerfOnCrit = firstEvalAltPerf.get(j);
					String firstEvalAltPerfOnCritId = firstEvalAltPerfOnCrit.getCriterionID();
					X2Value firstEvalAltPerfOnCritIdValue = firstEvalAltPerfOnCrit.getValue();
					// Put the Alternative-Criteria pair in the tableEval
					Criterion c = new Criterion(Integer.parseInt(firstEvalAltPerfOnCritId.substring(1)));
					double v = firstEvalAltPerfOnCritIdValue.getReal();
					mcp.putValue(a, c, v);
				}
			}
		} // end try
		
		return mcp;
				
	} // end marshalAndStore
	
	public static void main(String[] args) throws JAXBException, FileNotFoundException, IOException{
		Unmarshalling2 u = new Unmarshalling2();
		MCProblem unmarshalledMcp = u.unmarshalAndStore();
		System.out.println(unmarshalledMcp.getTableEval());
	}
}
