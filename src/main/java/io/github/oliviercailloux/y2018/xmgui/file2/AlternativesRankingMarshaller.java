package io.github.oliviercailloux.y2018.xmgui.file2;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeValue;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

public class AlternativesRankingMarshaller {
	
	private AlternativesRanking AltR;
	final ObjectFactory f = new ObjectFactory();
	final XMCDA xmcda = f.createXMCDA();
	
	public AlternativesRankingMarshaller(AlternativesRanking AltR) {
		Objects.requireNonNull(AltR);
		this.AltR = AltR;
	}
    
	public X2Value createX2Value(int Value){
		X2Value X2Value = new X2Value();
		X2Value.setInteger(Value);
		return X2Value;
	}

	/**
	 * This method marshalls AlternativesRanking objects to output an XML file abiding by the XMCDA standard.
	 * 
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeAlternativeValue(OutputStream fos) throws JAXBException {
	
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Marshaller marshaller = jc.createMarshaller();
		final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
		ImmutableSetMultimap<Integer, Alternative> map = AltR.getAltSet();

		for (int Rank : map.keySet()) {
			ImmutableSet<Alternative> allAlt = map.get(Rank);
			for (Alternative Alt : allAlt){
				X2AlternativeValue X2AltV = f.createX2AlternativeValue();
				X2AltV.setAlternativeID(Integer.toString(Alt.getId()));
				X2AltV.getValueOrValues().add(createX2Value(Rank));
				xmcdaSubElements.add(f.createXMCDAAlternativeValue(X2AltV));
			}
		}
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(xmcda, fos);
	}
	
	/*
	 * This method outputs AlternativesRanking's values in a Node format 
	 * to be used in Diviz's web services call through the CallAltsRank class.
	 * 
	 * @param altr the AlternativesRanking from which the alternatives and values are extracted
	 * @param doc the document used in the CallAltsRank class
	 * @return the XMCDA node to be used in the CallAltsRank class
	 */
	public Element altsRankingNodeForWSCall(AlternativesRankingMarshaller altr, Document doc) {
		
		Element XMCDANode = doc.createElement("xmcda:XMCDA");
		Element alternativesValuesNode = doc.createElement("alternativesValues");
		Attr overValues = doc.createAttribute("mcdaConcept");
		overValues.setValue("overallValues");
		alternativesValuesNode.setAttributeNode(overValues);
		ImmutableSetMultimap<Integer, Alternative> map = AltR.getAltSet(); // AltR shouldn't be called from the class but passed as a parameter.
		
		for (int Rank : map.keySet()) {
			ImmutableSet<Alternative> allAlt = map.get(Rank);
			for (Alternative Alt : allAlt){
				X2AlternativeValue X2AltV = f.createX2AlternativeValue();
				X2AltV.setAlternativeID(Integer.toString(Alt.getId()));
				X2AltV.getValueOrValues().add(createX2Value(Rank));
				Element alternativeNode=doc.createElement("alternativeValue");
				Element altIdNode=doc.createElement("alternativeID");
				altIdNode.appendChild(doc.createTextNode(X2AltV.getAlternativeID()));
				Element valueNode=doc.createElement("value");
				Element realNode=doc.createElement("real");
				realNode.appendChild(doc.createTextNode(Integer.toString(Rank)));
				valueNode.appendChild(realNode);
				alternativeNode.appendChild(altIdNode);
				alternativeNode.appendChild(valueNode);
				alternativesValuesNode.appendChild(alternativeNode);
			}
		}
		
		XMCDANode.appendChild(alternativesValuesNode);
		XMCDANode.setAttribute("xmlns:xmcda", "http://www.decision-deck.org/2012/XMCDA-2.2.1");
		return XMCDANode;
	}
}
