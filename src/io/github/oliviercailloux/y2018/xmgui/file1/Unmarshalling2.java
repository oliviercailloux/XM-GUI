package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;


public class Unmarshalling2 {
	
	private MCProblem mcp;

	public Unmarshalling2(MCProblem mcp) {
		this.mcp = mcp;
	}
	
	public void unmarshalAndStore() throws JAXBException, FileNotFoundException, IOException {
		
		
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		// final ObjectFactory f = new ObjectFactory();
		
		try (final FileInputStream fis = new FileInputStream( new File("C:/Users/Lamrani/3D Objects/XM-GUI/resources/resourcesfile1/test2.xml"))) {
			
			final XMCDA xmcda = (XMCDA) unmarshaller.unmarshal(fis);
			final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
			
			// Get X2Alternatives
			System.out.println(xmcdaSubElements.get(0).getValue());
			X2Alternatives alts = (X2Alternatives) xmcdaSubElements.get(0).getValue();
			
			// Drilldown to get List of X2Alternative
			List<Object> altsList = alts.getDescriptionOrAlternative();
			System.out.println(altsList);
			
			//Drilldown to get the first X2Alternative
			X2Alternative firstAlt = (X2Alternative) altsList.get(0);
			System.out.println(firstAlt);
			X2Alternative secondAlt = (X2Alternative) altsList.get(1);
			System.out.println(firstAlt);
			
			//Drilldown to get the first X2Alternative's id
			String firstAltId = firstAlt.getId();
			System.out.println(firstAltId);
			
			
		/*
		final X2Alternatives alternatives = f.createX2Alternatives();
		final X2Alternative alt = f.createX2Alternative();
		// for (X2Alternative alt : alternatives) {
			Alternative a = new Alternative(Integer.parseInt(alt.getId().substring(1)));
			mcp.addAlt(a);
		
		final X2Criteria criteria = f.createX2Criteria();
		final X2Criterion crit = f.createX2Criterion();
		// for (X2Alternative alt : alternatives) {
			Criterion c = new Criterion(Integer.parseInt(crit.getId().substring(1)));
			mcp.addCrit(c);
			
		final XMCDA xmcda = f.createXMCDA();
		final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
		xmcdaSubElements.add(f.createXMCDAAlternatives(alternatives));
		xmcdaSubElements.add(f.createXMCDACriteria(criteria));
		 */
		}
	}
	
	public static void main(String[] args) throws JAXBException, FileNotFoundException, IOException{
		MCProblem mcp = new MCProblem();
		Unmarshalling2 u = new Unmarshalling2(mcp);
		u.unmarshalAndStore();
	}

}