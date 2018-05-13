package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

import com.google.common.collect.UnmodifiableIterator;
import com.google.common.io.Files;

/*
 * This class test the File1 functionality: marshalling and unmarshalling of an MCProblem.
 */
public class App {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	/*
	 * Creates a MCProblem objects, marshalls it to an XML file whose path is specified, and unmarshalls it.
	 * Verification is done visually by printing the whole tableEval of the MCProblem object.
	 */
	public static void main(String[] args) throws FileNotFoundException, JAXBException, IOException {
		
		String path="MCPFile.xml";
		Path filepath= Paths.get(path);
		
		Alternative alt= new Alternative(1);
		Criterion crt =new Criterion(1);
		Criterion crt2 = new Criterion(1);
		Criterion crt3 = new Criterion(1);
		Criterion crt4 = new Criterion(2);
		Criterion crt5 = new Criterion(3);
		Alternative alt2 = new Alternative(2);
		Alternative alt3 = new Alternative(3);
		MCProblem mcp = new MCProblem();
		mcp.putEvaluation(alt, crt, 2.0f);
		mcp.putEvaluation(alt2, crt2, 13.3f);
		mcp.putEvaluation(alt3, crt3, 6f);
		mcp.putEvaluation(alt3, crt4, 12f);
		mcp.putEvaluation(alt3, crt5, 120f);
		LOGGER.info("MCP instance created");
		
		MCProblemMarshaller mcpMarshaller = new MCProblemMarshaller(mcp);
		try (final FileOutputStream fos = new FileOutputStream(path)) {
			mcpMarshaller.marshalAndWrite(fos);
			LOGGER.debug("Marshalling invoked");
		}
		
		MCProblemUnmarshaller mcpUnmarshaller = new MCProblemUnmarshaller();
		try (InputStream in = java.nio.file.Files.newInputStream(filepath)) {
			mcpUnmarshaller.readMCProblemFromXml(in);
			LOGGER.debug("Unmarshalling invoked");
		}
		
		System.out.println(mcp.toStringTableEval());
	}

}
