package io.github.oliviercailloux.y2018.xmgui.file1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;


import java.net.URL;

import javax.xml.bind.JAXBException;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

import org.junit.Test;

import com.google.common.collect.UnmodifiableIterator;


public class File1Test {

	@Test
	public void test() throws FileNotFoundException, JAXBException, IOException {
		URL resourceUrl= Marshalling.class.getResource("/io/github/oliviercailloux/y2018/xmgui/file1.xml");
		Alternative alt= new Alternative(1);
		Criterion crt =new Criterion(1);
		Criterion crt2 = new Criterion(2);
		Alternative alt2 = new Alternative(2);
		MCProblem mcp = new MCProblem();
		mcp.putValue(alt, crt, 2.0f);
		mcp.putValue(alt2, crt2, 13.3f);
		Marshalling tm = new Marshalling(mcp);
		tm.marshalAndWrite(resourceUrl.getFile());
		
		//lecture de file1
		Unmarshalling u = new Unmarshalling();
		MCProblem unmarshalledMcp = u.unmarshalAndStore(resourceUrl.getFile());
		
		UnmodifiableIterator<Alternative> it =unmarshalledMcp.getTableEval().rowKeySet().iterator();
		Alternative a=it.next();
		assertEquals(alt.getId(), a.getId());
		a=it.next();
		assertEquals(alt2.getId(), a.getId());
		assertEquals(mcp.getValueList(alt2).values(),unmarshalledMcp.getValueList(a).values());
		
		


	}

}
