package io.github.oliviercailloux.y2018.xmgui.WSCall;

import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.soap.SOAPBinding;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.google.common.io.Resources;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;
import io.github.oliviercailloux.y2018.xmgui.file1.MCProblemMarshaller;
import io.github.oliviercailloux.y2018.xmgui.file2.AlternativesRankingMarshaller;

/*
 * This class is used to invoke the decision deck's web services (currently only 'plotNumericPerformanceTable' from ITTB) with valid XMCDA documents
 * corresponding to the methodParameters, Alternatives, Criteria and performanceTable of a multi-criteria problem (MCProblem object).
 */
public class AppWSCall {
	
	private static final String ENDPOINT_ADDRESS = "http://webservices.decision-deck.org/soap/plotNumericPerformanceTable-ITTB.py";
	@SuppressWarnings("unused")
	private static final String FAILURE = "The problem submission was unsuccessful";
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AppWSCall.class);
	private static final String SUCCESS = "The problem submission was successful!";
	private Transformer transformer;
	
	public AppWSCall() {
		transformer = null;
	}
	
	public Transformer getTransformer() throws TransformerFactoryConfigurationError, TransformerConfigurationException {
		if (transformer == null) {
			final TransformerFactory tFactory = TransformerFactory.newInstance();
			transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		}
		return transformer;
	}
	
	public String asString(Node node) throws TransformerException, TransformerFactoryConfigurationError, TransformerConfigurationException {
		final StringWriter asString = new StringWriter();
		getTransformer().transform(new DOMSource(node), new StreamResult(asString));
		return asString.toString();
	}
	
	public Node invoke(Dispatch<Source> dispatch, Source src) throws TransformerException, TransformerFactoryConfigurationError, TransformerConfigurationException {
		final Source ret = dispatch.invoke(src);
		final DOMResult result = new DOMResult();
		getTransformer().transform(ret, result);
		final Node resultNode = result.getNode();
		return resultNode;
	}
	
	public void setFileContentToNodeValue(String sourceFile, Node destNode) throws IOException {
 		final URL resUrl = getClass().getResource(sourceFile);
 		final String resStr = Resources.toString(resUrl, StandardCharsets.UTF_8);
 		final Text textNode = destNode.getOwnerDocument().createCDATASection(resStr);
 		destNode.appendChild(textNode);
 	}
 
 	/*
 	 * This method tests the availability of the decision-deck's plotNumericPerformanceTable web service.
 	 */
 	@Test
 	public void testHello() throws Exception {
 		final Service svc = Service.create(new QName("ServiceNamespace", "ServiceLocalPart"));
 		final QName portQName = new QName("PortNamespace", "PortLocalPart");
 		svc.addPort(portQName, SOAPBinding.SOAP11HTTP_BINDING, ENDPOINT_ADDRESS);
 		final Dispatch<Source> dispatch = svc.createDispatch(portQName, Source.class, Mode.PAYLOAD);
 		final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
 		final Document doc = builder.newDocument();
 		final Element el = doc.createElement("hello");
 		doc.appendChild(el);
 		final DOMSource src = new DOMSource(doc);
 
 		final Node resultNode = invoke(dispatch, src);
 
 		LOGGER.info("Returned answer: {}.", asString(resultNode));
 
 		final NodeList directChildren = resultNode.getChildNodes();
 		assertEquals(1, directChildren.getLength());
 		final Node firstChild = directChildren.item(0);
 		assertEquals("helloResponse", firstChild.getNodeName());
 		final NodeList subChildren = firstChild.getChildNodes();
 		assertEquals(1, subChildren.getLength());
 		final Node firstSubChild = subChildren.item(0);
 		assertEquals("message", firstSubChild.getNodeName());
 	}
	
 	/*
 	 * This methods submits a static multi-criteria problem to the endpoint address (currently the decision-deck's plotNumericPerformanceTable web service) and request a response.
 	 * This MCProblem abides by the XMCDA standard and is submitted containing its methodParameters, Alternatives, Criteria and performanceTable documents.
 	 */
	@Test
	public void SubmitAndRequest() throws Exception {
		final Service svc = Service.create(new QName("ServiceNamespace", "ServiceLocalPart"));
		final QName portQName = new QName("PortNamespace", "PortLocalPart");
		svc.addPort(portQName, SOAPBinding.SOAP11HTTP_BINDING, ENDPOINT_ADDRESS);
		final Dispatch<Source> dispatch = svc.createDispatch(portQName, Source.class, Mode.PAYLOAD);
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final String ticket;
		
		{
			// For now, a raw MCProblem is created to be sent to the decision-deck plotNumericPerformanceTable web service.
			// The evaluationGUI will provide such MCProblem in the future.
			MCProblem mcp = new MCProblem();
			Alternative alt0 = new Alternative(0);
			Alternative alt1 = new Alternative(1);
			Alternative alt2 = new Alternative(2);
			Criterion crt0 = new Criterion(0);
			Criterion crt1 = new Criterion(1);
			mcp.putEvaluation(alt0, crt0, 0.0f);
			mcp.putEvaluation(alt1, crt0, 1.0f);
			mcp.putEvaluation(alt2, crt0, 2.0f);
			mcp.putEvaluation(alt0, crt1, 10.0f);
			mcp.putEvaluation(alt1, crt1, 11.0f);
			mcp.putEvaluation(alt2, crt1, 22.0f);
			MCProblemMarshaller mcpMarshaller= new MCProblemMarshaller(mcp);
			
			final Document doc = builder.newDocument();
			
			// Create the problem to be submitted to the web service and set its attributes
			final Element submit = doc.createElement("submitProblem");
			submit.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
			final Attr attrType1 = doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type");
			attrType1.setValue("xsd:string");
			doc.appendChild(submit);

			// Create the methodParameters XMCDA document and set its attributes
			final Element subMethParameters = doc.createElement("methodParameters");
			submit.appendChild(subMethParameters);
			CDATASection cdataMethParameters = doc.createCDATASection(asString(CreateMethodParamatersNode.plotMethodParametersNodeForWSCall(doc)));
			subMethParameters.appendChild(cdataMethParameters);
			final Attr attrMethParameters = doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type");
			attrMethParameters.setValue("xsd:string");
			subMethParameters.setAttributeNodeNS(attrMethParameters);
			Attr methParamsTag = doc.createAttribute("tag");
			methParamsTag.setValue("methodParameters");
			subMethParameters.setAttributeNode(methParamsTag);
			
			// Create the alternatives XMCDA document and set its attributes
			final Element sub2 = doc.createElement("alternatives");
			submit.appendChild(sub2);
			CDATASection cdataAlt = doc.createCDATASection(asString(mcpMarshaller.altsNodeForWSCall(mcp, doc)));
			sub2.appendChild(cdataAlt);
			final Attr attrSub2 = doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type");
 			attrSub2.setValue("xsd:string");
			sub2.setAttributeNodeNS(attrSub2);
			Attr altsTag = doc.createAttribute("tag");
			altsTag.setValue("alternatives");
			sub2.setAttributeNode(altsTag);
			
			// Create the criteria XMCDA document and set its attributes
			final Element sub4 = doc.createElement("criteria");
			submit.appendChild(sub4);
			CDATASection cdataCriteria = doc.createCDATASection(asString(mcpMarshaller.critNodeForWSCall(mcp, doc)));
			sub4.appendChild(cdataCriteria);
			final Attr attrSub4 = doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type");
			attrSub4.setValue("xsd:string");
			sub4.setAttributeNodeNS(attrSub4);
			Attr criteriaTag = doc.createAttribute("tag");
			criteriaTag.setValue("criteria");
			sub4.setAttributeNode(criteriaTag);
			
			// Create the performanceTable XMCDA document and set its attributes
			final Element sub3 = doc.createElement("performanceTable");
			submit.appendChild(sub3);
			CDATASection cdataPerfTable = doc.createCDATASection(asString(mcpMarshaller.perfTableNodeForWSCall(mcp, doc)));
			sub3.appendChild(cdataPerfTable);
			final Attr attrSub3 = doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type");
			attrSub3.setValue("xsd:string");
			sub3.setAttributeNodeNS(attrSub3);
			Attr perfTableTag = doc.createAttribute("tag");
			perfTableTag.setValue("performanceTable");
			sub3.setAttributeNode(perfTableTag);

			LOGGER.info("Sending: {}.", asString(doc));

			final Node ret = invoke(dispatch, new DOMSource(doc));
			
			LOGGER.info("Returned answer: {}.", asString(ret));
			
			final NodeList directChildren = ret.getChildNodes();
			assertEquals(1, directChildren.getLength());
			final Node firstChild = directChildren.item(0);
			assertEquals("submitProblemResponse", firstChild.getNodeName());
			final NodeList subChildren = firstChild.getChildNodes();
			assertEquals(2, subChildren.getLength());
			final Node firstSubChild = subChildren.item(0);
			assertEquals("message", firstSubChild.getNodeName());
			final NodeList messageChildren = firstSubChild.getChildNodes();
			assertEquals(1, messageChildren.getLength());
			final Node messageText = messageChildren.item(0);
			assertEquals(SUCCESS, messageText.getNodeValue());
			final Node secondSubChild = subChildren.item(1);
			assertEquals("ticket", secondSubChild.getNodeName());
			ticket = secondSubChild.getFirstChild().getTextContent();
			
			LOGGER.info("Ticket: {}.", ticket);
		}
		
	final Document requestSolutionDoc = builder.newDocument();
	final Element requestSolution = requestSolutionDoc.createElement("requestSolution");
	requestSolution.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd","http://www.w3.org/2001/XMLSchema");
	final Element ticketEl = requestSolutionDoc.createElement("ticket");
	requestSolutionDoc.appendChild(requestSolution);
	requestSolution.appendChild(ticketEl);
	final Text ticketTextNode = requestSolutionDoc.createTextNode(ticket);
	ticketEl.appendChild(ticketTextNode);
	final Attr attrType1 = requestSolutionDoc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance","xsi:type");
	attrType1.setValue("xsd:string");
	ticketEl.setAttributeNodeNS(attrType1);
	
	LOGGER.info("Sending: {}.", asString(requestSolutionDoc));

	final Node solution = invoke(dispatch, new DOMSource(requestSolutionDoc));

	LOGGER.info("Returned answer: {}.", asString(solution));

	final NodeList directChildren = solution.getChildNodes();
	assertEquals(1, directChildren.getLength());
	final Node requestSolutionResponse = directChildren.item(0);
	assertEquals("requestSolutionResponse", requestSolutionResponse.getNodeName());
	final NodeList subChildren = requestSolutionResponse.getChildNodes();
	final Node alternativesRanks = subChildren.item(0);
	final NodeList alternativesRanksContentList = alternativesRanks.getChildNodes();
	assertEquals(1, alternativesRanksContentList.getLength());
	final Node alternativesRanksContent = alternativesRanksContentList.item(0);
	assertEquals(Node.TEXT_NODE, alternativesRanksContent.getNodeType());

	LOGGER.info("Content of returned alternativesRanks: {}.", alternativesRanksContent.getNodeValue());
	
	}
}

		
