package io.github.oliviercailloux.y2018.xmgui.WSCall;

import static org.junit.Assert.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.UnmodifiableIterator;

import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

/*
 * This class serves for decision-deck's plotNumericPerformanceTable web service invocation.
 * This web service requires a methodParameters XMCDA document which this class provides as a Node.
 */
public class CreateMethodParamatersNode {

	public static Element plotMethodParametersNodeForWSCall(Document doc) {
		
		Element XMCDANode = doc.createElement("xmcda:XMCDA");
		Element plotMethodParametersNode = doc.createElement("methodParameters");
		XMCDANode.appendChild(plotMethodParametersNode);
		
		// Create chart_title mandatory parameter
		Element parameter1 = doc.createElement("parameter");
		Attr parameter1Id = doc.createAttribute("id");
		parameter1Id.setValue("chart_title");
		parameter1.setAttributeNode(parameter1Id);
		plotMethodParametersNode.appendChild(parameter1);
		Element value1Node=doc.createElement("value");
		parameter1.appendChild(value1Node);
		Element label1Node =doc.createElement("label");
		value1Node.appendChild(label1Node);
		label1Node.appendChild(doc.createTextNode("Performance table plot"));
		
		// Create domain_axis mandatory parameter
		Element parameter2 = doc.createElement("parameter");
		Attr parameter2Id = doc.createAttribute("id");
		parameter2Id.setValue("domain_axis");
		parameter2.setAttributeNode(parameter2Id);
		plotMethodParametersNode.appendChild(parameter2);
		Element value2Node=doc.createElement("value");
		parameter2.appendChild(value2Node);
		Element label2Node =doc.createElement("label");
		value2Node.appendChild(label2Node);
		label2Node.appendChild(doc.createTextNode("Alternatives"));
		
		// Create range_axis mandatory parameter
		Element parameter3 = doc.createElement("parameter");
		Attr parameter3Id = doc.createAttribute("id");
		parameter3Id.setValue("range_axis");
		parameter3.setAttributeNode(parameter3Id);
		plotMethodParametersNode.appendChild(parameter3);
		Element value3Node=doc.createElement("value");
		parameter3.appendChild(value3Node);
		Element label3Node =doc.createElement("label");
		value3Node.appendChild(label3Node);
		label3Node.appendChild(doc.createTextNode("Performance on criteria"));

		XMCDANode.setAttribute("xmlns:xmcda", "http://www.decision-deck.org/2012/XMCDA-2.2.1"); 
		return XMCDANode;
	}
}
