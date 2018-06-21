package io.github.oliviercailloux.y2018.xmgui.WSCall;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

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

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;
import io.github.oliviercailloux.y2018.xmgui.file1.MCProblemMarshaller;
import io.github.oliviercailloux.y2018.xmgui.file2.AlternativesRankingMarshaller;


public class AppWSCall {
	
	private static final String WEBSERVICE ="plotNumericPerformanceTable.py";
	private static final String ENDPOINT_ADDRESS = "http://webservices.decision-deck.org/soap/" + WEBSERVICE;
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
			// For the time being we create a raw MCP here, but in the future, the GUI will create and pass this data.
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
			
			/*
			 * NON NECESSAIRE POUR INVOQUER plotNumericPerformanceTable
			 * 
			// These values should be given by the WS response after we invoke WeightedSum, but for now we give them raw (testing)
			// We will need to modify if as we won't have an AlternativesRanking but just One value for each alt
			AlternativesRanking altr = new AlternativesRanking(1,alt0);
			altr.putAltRank(2,alt1);
			altr.putAltRank(3,alt2);
			AlternativesRankingMarshaller altrMarshaller = new AlternativesRankingMarshaller(altr);
			*
			*/
			
			final Document doc = builder.newDocument();
			final Element submit = doc.createElement("submitProblem");
			submit.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
			
			final Element sub1 = doc.createElement("overallValues");
			
			/*
			 * NON NECESSAIRE POUR INVOQUER plotNumericPerformanceTable
			 *
			CDATASection cdataOverVal = doc.createCDATASection(asString(altrMarshaller.altsRankingNodeForWSCall(altrMarshaller, doc)));
			sub1.appendChild(cdataOverVal);
			*
			*/
			doc.appendChild(submit);
			
			final Element subMethParameters = doc.createElement("methodParameters");
			submit.appendChild(subMethParameters);
			CDATASection cdataMethParameters = doc.createCDATASection(asString(CreateMethodParamatersNode.plotMethodParametersNodeForWSCall(doc)));
			subMethParameters.appendChild(cdataMethParameters);
			
			final Element sub2 = doc.createElement("alternatives");
			submit.appendChild(sub2);
			CDATASection cdataAlt = doc.createCDATASection(asString(mcpMarshaller.altsNodeForWSCall(mcp, doc)));
			sub2.appendChild(cdataAlt);
			
			final Element sub4 = doc.createElement("criteria");
			submit.appendChild(sub4);
			CDATASection cdataCriteria = doc.createCDATASection(asString(mcpMarshaller.critNodeForWSCall(mcp, doc)));
			sub4.appendChild(cdataCriteria);
			
			final Element sub3 = doc.createElement("performanceTable");
			submit.appendChild(sub3);
			CDATASection cdataPerfTable = doc.createCDATASection(asString(mcpMarshaller.perfTableNodeForWSCall(mcp, doc)));
			sub3.appendChild(cdataPerfTable);
			
			final Attr attrType1 = doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type");
			attrType1.setValue("xsd:string");
			sub1.setAttributeNodeNS(attrType1);
			final Attr attrType2 = (Attr) attrType1.cloneNode(true);
			sub2.setAttributeNodeNS(attrType2);
			
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
		assertEquals(4, subChildren.getLength());
		final Node alternativesRanks = subChildren.item(0);
		assertEquals("alternativesRanks", alternativesRanks.getNodeName());
		final NodeList alternativesRanksContentList = alternativesRanks.getChildNodes();
		assertEquals(1, alternativesRanksContentList.getLength());
		final Node alternativesRanksContent = alternativesRanksContentList.item(0);
		assertEquals(Node.TEXT_NODE, alternativesRanksContent.getNodeType());

		LOGGER.info("Content of returned alternativesRanks: {}.", alternativesRanksContent.getNodeValue());
	}
}
