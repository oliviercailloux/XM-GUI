package io.github.oliviercailloux.y2018.xmgui.file1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

import org.junit.Test;

import com.google.common.collect.UnmodifiableIterator;


public class File1Test {

	@Test
	public void test() throws FileNotFoundException, JAXBException, IOException {
		String path="MCPFile.xml";
		Path filepath= Paths.get(path);
		Alternative alt= new Alternative(100000);
		Criterion crt =new Criterion(1);
		Criterion crt2 = new Criterion(2);
		Criterion crt3 = new Criterion(3);
		Alternative alt2 = new Alternative(2);
		Alternative alt3 = new Alternative(3);
		MCProblem mcp = new MCProblem();
		
		
		mcp.putEvaluation(alt, crt, 2.0f);
		mcp.putEvaluation(alt2, crt2, 13.3f);
		mcp.putEvaluation(alt3, crt3, 18042018f);
		
		MCProblemMarshaller tm = new MCProblemMarshaller(mcp);
		
		
		try (final FileOutputStream fos = new FileOutputStream(path)) {
			tm.marshalAndWrite(fos);
			
		}
		//lecture de file1
		MCProblemUnmarshaller u = new MCProblemUnmarshaller();
		MCProblem unmarshalledMcp=null;
		
		try (InputStream in = java.nio.file.Files.newInputStream(filepath)) {
			 unmarshalledMcp = u.readMCProblemFromXml(in);
		}
		
		
		UnmodifiableIterator<Alternative> it =unmarshalledMcp.getTableEval().rowKeySet().iterator();
		Alternative a=it.next();
		assertEquals(alt.getId(), a.getId());
		a=it.next();
		assertEquals(alt2.getId(), a.getId());
		assertEquals(mcp.getValueList(alt2).values(),unmarshalledMcp.getValueList(a).values());
}

}
