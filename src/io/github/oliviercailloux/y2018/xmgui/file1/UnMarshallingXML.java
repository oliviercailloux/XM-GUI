package io.github.oliviercailloux.y2018.xmgui.file1;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;
import java.util.Iterator;


// package io.github.oliviercailloux.xmcda_2_2_1_jaxb.marshal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

// import org.junit.Test;


import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternative;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Alternatives;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criteria;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Criterion;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;

public class UnMarshallingXML {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {

	    try (final FileInputStream fos = new FileInputStream(new File("/Users/Razorin/Desktop/PERSO/MIAGE/DEV/JAVA/XM-GUI/resources/resourcesfile1/test2.xml"))) {
	    	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fos);
		
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		
		NodeList nList = doc.getElementsByTagName("alternative");
		
		System.out.println("Alternatives");
		System.out.println("-----------");
	
		for (int i = 0; i < nList.getLength(); i++ ) {
			Node nNode = nList.item(i);
			Element eElement = (Element) nNode;
			System.out.println("AlternativeID : " + eElement.getAttribute("id"));
		} 
		
		System.out.println();
		
		nList = doc.getElementsByTagName("criterion");
		
		System.out.println("Criteria");
		System.out.println("-----------");
	
		for (int i = 0; i < nList.getLength(); i++ ) {
			Node nNode = nList.item(i);
			Element eElement = (Element) nNode;
			System.out.println("CriterionID : " + eElement.getAttribute("id"));
	    }


	/**
	RECUPERER LES CRITERION ET ALTERNATIVES POUR LES RANGER DANS DES ELEMENTS X2 + LES RETURN

	public void unMarshal() throws JAXBException, FileNotFoundException, IOException {
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Unmarshaller unMarshaller = jc.createUnMarshaller();
		final 
	}
	*/
		
	    }
	}
}

