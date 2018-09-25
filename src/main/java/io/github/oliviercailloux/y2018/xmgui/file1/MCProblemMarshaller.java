package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.UnmodifiableIterator;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeOnCriteriaPerformances;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternatives;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criteria;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2PerformanceTable;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

/*
 * This class serves the marshalling of multi-criteria problems (MCProblem objects).
 */
public class MCProblemMarshaller {

	/*
	 * The Multi-Criteria Problem to be marshalled.
	 */
	final JAXBContext jc;
	final Marshaller marshaller;
	private MCProblem mcp;

	public MCProblemMarshaller(MCProblem mcp) throws JAXBException {
		Objects.requireNonNull(mcp);
		this.mcp = mcp;
		jc = JAXBContext.newInstance(XMCDA.class);
		marshaller = jc.createMarshaller();
	}

	protected static final ObjectFactory f = new ObjectFactory();

	/**
	 * This method marshalls Alternative and Criterion objects and their respective
	 * performance pair values from the MCProblem object to output an XML file
	 * abiding by the XMCDA standard.
	 * 
	 * @param fos the XML file output
	 * @throws JAXBException
	 */
	public void marshalAndWrite(FileOutputStream fos) throws JAXBException {

		// Add X2Alternative objects
		final X2Alternatives alternatives = f.createX2Alternatives();
		UnmodifiableIterator<Alternative> itAlts = mcp.getAlternatives().iterator();
		while (itAlts.hasNext()) {
			Alternative a = itAlts.next();
			alternatives.getDescriptionOrAlternative().add(BasicObjectsMarshallerToX2.basicAlternativeToX2(a));
		}

		// Add X2Criterion objects
		final X2Criteria criteria = f.createX2Criteria();
		UnmodifiableIterator<Criterion> itCrits = mcp.getCriteria().iterator();
		while (itCrits.hasNext()) {
			criteria.getCriterion().add(BasicObjectsMarshallerToX2.basicCriterionToX2(itCrits.next()));
		}

		// Add X2Performances
		final X2PerformanceTable perfTable = f.createX2PerformanceTable();
		for (Alternative a : mcp.getAlternatives()) {
			X2AlternativeOnCriteriaPerformances performances = f.createX2AlternativeOnCriteriaPerformances();
			UnmodifiableIterator<Entry<Criterion, Float>> itCritsPerf = mcp.getTableEval().row(a).entrySet().iterator();
			while (itCritsPerf.hasNext()) {
				performances.getPerformance().add(CreatePerformance.createPerformance(itCritsPerf.next()));
				performances.setAlternativeID(Integer.toString(a.getId()));
			}
			perfTable.getAlternativePerformances().add(performances);
		}

		// Output the corresponding XMCDA file
		final XMCDA xmcda = f.createXMCDA();
		final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
		xmcdaSubElements.add(f.createXMCDAAlternatives(alternatives));
		xmcdaSubElements.add(f.createXMCDACriteria(criteria));
		xmcdaSubElements.add(f.createXMCDAPerformanceTable(perfTable));
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(xmcda, fos);
	}

	/*
	 * This method outputs a set of alternatives in a Node format to be used in
	 * Diviz's web services call through the WSCall and WSCallRank packages'
	 * classes.
	 * 
	 * @param mcp the MCProblem from which the alternatives set is extracted
	 * 
	 * @param doc the document to be sent to web services
	 * 
	 * @return the XMCDA node to be used in web sevices calls
	 */
	public Element altsNodeForWSCall(MCProblem mcpNode, Document doc) {
		Element XMCDANode = doc.createElement("xmcda:XMCDA");
		Element alternativesNode = doc.createElement("alternatives");
		XMCDANode.appendChild(alternativesNode);

		UnmodifiableIterator<Alternative> itAlts = mcpNode.getAlternatives().iterator();
		while (itAlts.hasNext()) {
			Alternative a = itAlts.next();
			Element alternativeNode = doc.createElement("alternative");
			Attr altId = doc.createAttribute("id");
			altId.setValue(Integer.toString(a.getId()));
			alternativeNode.setAttributeNode(altId);
			alternativesNode.appendChild(alternativeNode);
		}
		XMCDANode.setAttribute("xmlns:xmcda", "http://www.decision-deck.org/2012/XMCDA-2.2.1");
		return XMCDANode;
	}

	/*
	 * This method outputs a set of criteria in a Node format to be used in Diviz's
	 * web services call through the WSCall and WSCallRank packages' classes.
	 * 
	 * @param mcp the MCProblem from which the alternatives set is extracted
	 * 
	 * @param doc the document to be sent to web services
	 * 
	 * @return the XMCDA node to be used in web services call
	 */
	public Element critNodeForWSCall(MCProblem mcpNode, Document doc) {
		Element XMCDANode = doc.createElement("xmcda:XMCDA");
		Element criteriaNode = doc.createElement("criteria");
		XMCDANode.appendChild(criteriaNode);

		UnmodifiableIterator<Criterion> itCrits = mcp.getCriteria().iterator();
		while (itCrits.hasNext()) {
			Element criterionNode = doc.createElement("criterion");
			Criterion crit = itCrits.next();
			Attr critId = doc.createAttribute("id");
			critId.setValue(Integer.toString(crit.getId()));
			criterionNode.setAttributeNode(critId);
			criteriaNode.appendChild(criterionNode);
			criteriaNode.appendChild(criterionNode);
		}
		XMCDANode.setAttribute("xmlns:xmcda", "http://www.decision-deck.org/2012/XMCDA-2.2.1");
		return XMCDANode;
	}

	/*
	 * This method outputs a performanceTable in a Node format to be used in Diviz's
	 * web services call through the WSCall and WSCallRank packages' classes.
	 * 
	 * @param mcp the MCProblem from which the alternatives set is extracted
	 * 
	 * @param doc the document to be sent to web services
	 * 
	 * @return the XMCDA node to be used in web services call
	 */
	public Element perfTableNodeForWSCall(MCProblem mcpNode, Document doc) {
		Element XMCDANode = doc.createElement("xmcda:XMCDA");
		Element perfTableNode = doc.createElement("performanceTable");
		XMCDANode.appendChild(perfTableNode);
		Attr perfTableTag = doc.createAttribute("mcdaConcept");
		perfTableTag.setValue("performanceTable");
		perfTableNode.setAttributeNode(perfTableTag);

		for (Alternative a : mcp.getAlternatives()) {
			int altid = a.getId();
			UnmodifiableIterator<Entry<Criterion, Float>> itCritsPerf = mcp.getTableEval().row(a).entrySet().iterator();
			Element alternativePerfNode = doc.createElement("alternativePerformances");
			Element altIdNode = doc.createElement("alternativeID");
			alternativePerfNode.appendChild(altIdNode);
			altIdNode.appendChild(doc.createTextNode(Integer.toString(altid)));

			while (itCritsPerf.hasNext()) {
				Entry<Criterion, Float> critAndVal = itCritsPerf.next();
				Element criterionIDNode = doc.createElement("criterionID");
				criterionIDNode.appendChild(doc.createTextNode(Integer.toString(critAndVal.getKey().getId())));
				Element perfNode = doc.createElement("performance");
				Element valueNode = doc.createElement("value");
				Element realNode = doc.createElement("real");
				realNode.appendChild(doc.createTextNode(Float.toString(critAndVal.getValue())));
				valueNode.appendChild(realNode);
				perfNode.appendChild(criterionIDNode);
				perfNode.appendChild(valueNode);
				alternativePerfNode.appendChild(perfNode);
			}
			perfTableNode.appendChild(alternativePerfNode);
		}
		XMCDANode.setAttribute("xmlns:xmcda", "http://www.decision-deck.org/2012/XMCDA-2.2.1");
		return XMCDANode;
	}

}