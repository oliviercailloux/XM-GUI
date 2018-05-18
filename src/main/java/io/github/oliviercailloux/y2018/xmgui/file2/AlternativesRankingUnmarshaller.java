package io.github.oliviercailloux.y2018.xmgui.file2;


import java.io.InputStream;
import java.util.List;
import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeValue;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;


public class AlternativesRankingUnmarshaller{
	
	private AlternativesRanking AltR;

	/**
	 * This method reads a ranking of alternatives in the form of an XML
	 * file abiding by the XMCDA standard.
	 * 
	 * @param in the InputStream corresponding to the XML file containing the multi-criteria problem
	 * @return the MCProblem object corresponding to the XML file transmitted
	 * @throws JAXBException
	 * 
	 */
	public AlternativesRanking readAlternativesRankingFromXml(InputStream in) throws JAXBException {
			
			final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
			final Unmarshaller unmarshaller = jc.createUnmarshaller();
			final XMCDA xmcda = (XMCDA) unmarshaller.unmarshal(in);
			final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
				
			
			// Read X2AlternativeValue
			List<X2AlternativeValue> X2AltVList = getX2AlternativeValue(xmcdaSubElements);
			for (X2AlternativeValue X2AltV : X2AltVList) {
				Alternative Alt = extractAltFromX2AlternativeValue(X2AltV);
				Integer Rank = extractRankFromX2AlternativeValue(X2AltV);
				if(Rank == 1){
					AltR = new AlternativesRanking(Rank,Alt);
				}
				AltR.putAltRank(Rank,Alt);			
			}
				
			return AltR;
					
	} 
		
		/*
		 * This method finds and reads the X2AlternativeValue item from an unmarshalled XML file
		 * abiding by the XMCDA standard.
		 * 
		 * @param xmcdaSubElements the unmarshalled project references of the XMCDA file
		 * @return the list of X2AlternativeValue to be read
		 */
		public List<X2AlternativeValue> getX2AlternativeValue(List<JAXBElement<?>> xmcdaSubElements) {
			int AltIndex = 0;
			List<X2AlternativeValue> X2AltVList = new ArrayList<X2AlternativeValue>();
			while (AltIndex < xmcdaSubElements.size()) {
				if ( xmcdaSubElements.get(AltIndex).getName().toString().equalsIgnoreCase("alternativeValue") ) {
					X2AlternativeValue X2AltV = (X2AlternativeValue) xmcdaSubElements.get(AltIndex).getValue();
					X2AltVList.add(X2AltV);
				}
				AltIndex++;
			} 
			return X2AltVList;
		}
		
		/*
		 * This method extracts the ID of the Alternative from 
		 * a list of X2AlternativeValue and create a corresponding Alternative object.
		 * 
		 * @param i the index of the X2Alternative's ID to be extracted
		 * @param x2AltsList the raw X2Alternatives list to be navigated
		 * @return the Alternative object created with the extracted ID
		 */
		public Alternative extractAltFromX2AlternativeValue(X2AlternativeValue X2AltV) {
			String AltID = X2AltV.getAlternativeID();
			Alternative Alt = new Alternative(Integer.parseInt(AltID));
			return Alt;
		}

		/*
		 * This method finds and reads the X2Criteria item from an unmarshalled XML file
		 * abiding by the XMCDA standard.
		 * 
		 * @param xmcdaSubElements the unmarshalled project references of the XMCDA file
		 * @return the list of X2Criteria to be read
		 */
		public Integer extractRankFromX2AlternativeValue(X2AlternativeValue X2AltV) {
			int Rank = 0;
			List<Object> X2ValueList = X2AltV.getValueOrValues();
			for(Object X2Value : X2ValueList){
				Rank = ((X2Value) X2Value).getInteger();
			}
			return Rank;
		}
	}

