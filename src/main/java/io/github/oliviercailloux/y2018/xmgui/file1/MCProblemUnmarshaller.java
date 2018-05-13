package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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


public class MCProblemUnmarshaller {
	
	private MCProblem mcp = new MCProblem();

	/**
	 * This method reads a multi-criteria problem in the form of an XML
	 * file abiding by the XMCDA standard.
	 * 
	 * @param in the InputStream corresponding to the XML file containing the multi-criteria problem
	 * @return the MCProblem object corresponding to the XML file transmitted
	 * @throws JAXBException
	 * 
	 */
	public MCProblem readMCProblemFromXml(InputStream in) throws JAXBException {
		
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		final XMCDA xmcda = (XMCDA) unmarshaller.unmarshal(in);
		final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
			
		// Read X2Alternatives
		X2Alternatives alts = getX2Alternatives(xmcdaSubElements);
		List<Object> x2AltsList = alts.getDescriptionOrAlternative();
		for (int i = 0; i < x2AltsList.size(); i++) {
			Alternative a = extractAltFromX2Alternatives(i, x2AltsList);
			mcp.addAlt(a);
		}
			
		// Read X2Criteria
		X2Criteria crits = getX2Criteria(xmcdaSubElements);
		List<X2Criterion> critsList = crits.getCriterion();
		for (int i = 0; i < critsList.size(); i++) {
			Criterion c = extractCritFromX2Criteria(i, critsList);
			mcp.addCrit(c);
		}

		// Read X2PerformanceTable
		X2PerformanceTable perfTable = getX2PerformanceTable(xmcdaSubElements);
		List<X2AlternativeOnCriteriaPerformances> altsOnCritsPerf = perfTable.getAlternativePerformances();
		for (int i = 0; i < altsOnCritsPerf.size(); i++) {
			getListOfX2AlternativePerformancesOnCriteriaAndPutInMcp(i, altsOnCritsPerf);
		}
			
		return mcp;
				
	} 
	
	
	/*
	 * This method finds and reads the X2Alternatives item from an unmarshalled XML file
	 * abiding by the XMCDA standard.
	 * 
	 * @param xmcdaSubElements the unmarshalled project references of the XMCDA file
	 * @return the list of X2Alternatives to be read
	 */
	public X2Alternatives getX2Alternatives(List<JAXBElement<?>> xmcdaSubElements) {
		int altsIndex = 0;
		while (altsIndex < xmcdaSubElements.size()) {
			if ( xmcdaSubElements.get(altsIndex).getName().toString().equalsIgnoreCase("alternatives") ) {
				break;
			}
			altsIndex++;
		} 
		X2Alternatives x2Alts = (X2Alternatives) xmcdaSubElements.get(altsIndex).getValue();
		return x2Alts;
	}
	
	/*
	 * This method extracts the ID of a specific X2Alternative from 
	 * a list of X2Alternatives and create a corresponding Alternative object.
	 * 
	 * @param i the index of the X2Alternative's ID to be extracted
	 * @param x2AltsList the raw X2Alternatives list to be navigated
	 * @return the Alternative object created with the extracted ID
	 */
	public Alternative extractAltFromX2Alternatives(int i, List<Object> x2AltsList) {
		X2Alternative x2Alt = (X2Alternative) x2AltsList.get(i);
		String x2AltId = x2Alt.getId();
		Alternative a = new Alternative(Integer.parseInt(x2AltId.substring(1)));
		return a;
	}

	/*
	 * This method finds and reads the X2Criteria item from an unmarshalled XML file
	 * abiding by the XMCDA standard.
	 * 
	 * @param xmcdaSubElements the unmarshalled project references of the XMCDA file
	 * @return the list of X2Criteria to be read
	 */
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
	
	/*
	 * This method extracts the ID of a specific X2Criterion from 
	 * a list of X2Criteria and create a corresponding Criterion object.
	 * 
	 * @param i the index of the X2Criterion's ID to be extracted
	 * @param x2CritsList the raw  X2Criteria list to be navigated
	 * @return the Criterion object created with the extracted ID
	 */
	public Criterion extractCritFromX2Criteria(int i, List<X2Criterion> x2CritsList) {
		X2Criterion x2Crit = x2CritsList.get(i);
		String x2CritId = x2Crit.getId();
		Criterion c = new Criterion(Integer.parseInt(x2CritId.substring(1)));		
		return c;
	}
	
	/*
	 * This method finds and reads the X2PerformanceTable item from an unmarshalled XML file
	 * abiding by the XMCDA standard.
	 * 
	 * @param xmcdaSubElements the unmarshalled project references of the XMCDA file
	 * @return the X2PerformanceTable to be read
	 */
	public X2PerformanceTable getX2PerformanceTable(List<JAXBElement<?>> xmcdaSubElements) {
		int perfsIndex = 0;
		while (perfsIndex < xmcdaSubElements.size()) {
			if ( xmcdaSubElements.get(perfsIndex).getName().toString().equalsIgnoreCase("performanceTable") ) {
				break;
				}
			perfsIndex++;
			} 
		X2PerformanceTable perfTable = (X2PerformanceTable) xmcdaSubElements.get(perfsIndex).getValue();		
		return perfTable;
	}
	
	/*
	 * This method extracts the values of a specific X2Alternative's performance on every X2Criterion
	 * and put every performance value in a MCProblem object, using Alternative's and Criterion's unique ID.
	 * 
	 * @param i the index of the specific X2Alternative in the list, from which performance values will be extracted
	 * @param altsOnCriteriaPerfs is the list of X2Alternative objects evaluated on all criteria
	 */
	public void getListOfX2AlternativePerformancesOnCriteriaAndPutInMcp(int i, List<X2AlternativeOnCriteriaPerformances> altsOnCriteriaPerfs) {
		X2AlternativeOnCriteriaPerformances evaluatedX2Alt = altsOnCriteriaPerfs.get(i);
		String evaluatedX2AltId = evaluatedX2Alt.getAlternativeID();
		Alternative evaluatedAlternative = new Alternative(Integer.parseInt(evaluatedX2AltId.substring(1)));
		List<X2AlternativeOnCriteriaPerformances.Performance> evaluatedX2AltPerfs = evaluatedX2Alt.getPerformance();		

		for (int j = 0; j < evaluatedX2AltPerfs.size(); j++) {
			
			X2AlternativeOnCriteriaPerformances.Performance evaluatedX2AltPerfOnX2Crit = evaluatedX2AltPerfs.get(j);
			String x2CritIdOfEvaluatedX2AltPerf = evaluatedX2AltPerfOnX2Crit.getCriterionID();
			X2Value valueOfEvaluatedX2AltPerfOnX2Crit = evaluatedX2AltPerfOnX2Crit.getValue();
			
			double performanceValue = valueOfEvaluatedX2AltPerfOnX2Crit.getReal();
			Criterion evaluatedCriterion = new Criterion(Integer.parseInt(x2CritIdOfEvaluatedX2AltPerf.substring(1)));

			mcp.putEvaluation(evaluatedAlternative, evaluatedCriterion, (float) performanceValue);
		}
		
	}
}
