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

public class App {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
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
		
		
		mcp.putValue(alt, crt, 2.0f);
		mcp.putValue(alt2, crt2, 13.3f);
		mcp.putValue(alt3, crt3, 6f);
		mcp.putValue(alt3, crt4, 12f);
		mcp.putValue(alt3, crt5, 120f);
		
		MCProblemMarshaller tm = new MCProblemMarshaller(mcp);
		LOGGER.debug("MCP instance created");
		
		try (final FileOutputStream fos = new FileOutputStream(path)) {
			tm.marshalAndWrite(fos);
			LOGGER.info("Marshalling invoked");
		}
		//lecture de file1
		MCProblemUnmarshaller u = new MCProblemUnmarshaller();
		
		try (InputStream in = java.nio.file.Files.newInputStream(filepath)) {
			MCProblem unmarshalledMcp = u.unmarshalAndStore(in);
			LOGGER.debug("Unmarshalling invoked");
			
		}
		
		//print tableEval
		System.out.println(mcp.toStringTableEval());
	}

}
