package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;

import com.google.common.collect.UnmodifiableIterator;

public class App {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws FileNotFoundException, JAXBException, IOException {
		Alternative alt= new Alternative(1);
		Criterion crt =new Criterion(1);
		Criterion crt2 = new Criterion(2);
		Alternative alt2 = new Alternative(2);
		MCProblem mcp = new MCProblem();
		mcp.putValue(alt, crt, 2.0f);
		mcp.putValue(alt, crt2, 3.0f);
		mcp.putValue(alt2, crt, 10.0f);
		mcp.putValue(alt2, crt2, 13.3f);
		Marshalling tm = new Marshalling(mcp);
		LOGGER.debug("MCP instance created");
		
		tm.marshalAndWrite();
		LOGGER.info("Marshalling invoked");
		
		//lecture de file1
		Unmarshalling u = new Unmarshalling();
		MCProblem unmarshalledMcp = u.unmarshalAndStore();
		LOGGER.debug("Unmarshalling invoked");

		
		UnmodifiableIterator<Alternative> it =unmarshalledMcp.getTableEval().rowKeySet().iterator();
		while(it.hasNext()){
			Alternative a=it.next();
			System.out.println("-------------------------------------------------------");
			System.out.println("Alternative : " + a.getId());
			for(Criterion c: unmarshalledMcp.getValueList(a).keySet().asList()) {
				System.out.println("Criterion : " + c.getId()); // erreur car il itère mal : il devrait ecrire les deux critères pour chaque alternative...
				System.out.println("Value : " + unmarshalledMcp.getValueList(a).values());
			}
			System.out.println("-------------------------------------------------------");
		}
		// Print la table entière pour vérifier que les Criterion et Alternative objects sont bien identifiés et uniques via l'ID
		System.out.println(mcp.getTableEval());
	}

}
