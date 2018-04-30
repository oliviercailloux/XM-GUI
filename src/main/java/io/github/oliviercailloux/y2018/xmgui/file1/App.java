package io.github.oliviercailloux.y2018.xmgui.file1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

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
		URL resourceUrl= Marshalling.class.getResource("/io/github/oliviercailloux/y2018/xmgui/file1.xml");
		Alternative alt= new Alternative(1);
		Criterion crt =new Criterion(1);
		Criterion crt2 = new Criterion(2);
		Criterion crt3 = new Criterion(3);
		Alternative alt2 = new Alternative(2);
		Alternative alt3 = new Alternative(3);
		MCProblem mcp = new MCProblem();
		
		mcp.putValue(alt, crt, 2.0f);
		mcp.putValue(alt2, crt2, 13.3f);
		mcp.putValue(alt3, crt3, 18042018f);
		
		Marshalling tm = new Marshalling(mcp);
		LOGGER.debug("MCP instance created");
		
		tm.marshalAndWrite(resourceUrl.getFile());
		LOGGER.info("Marshalling invoked");
		
		//lecture de file1
		Unmarshalling u = new Unmarshalling();
		MCProblem unmarshalledMcp = u.unmarshalAndStore(resourceUrl.getFile());
		LOGGER.debug("Unmarshalling invoked");

		
		UnmodifiableIterator<Alternative> it =unmarshalledMcp.getTableEval().rowKeySet().iterator();
		
		 //Mis en commentaire car erreur dans l'itération : il devrait ecrire tous les critères pour chaque alternative...
		   while(it.hasNext()){
			Alternative a=it.next();
			System.out.println("-------------------------------------------------------");
			System.out.println("Alternative : " + a.getId());
			for(Criterion c: unmarshalledMcp.getValueList(a).keySet().asList()) {
				System.out.println("Criterion : " + c.getId()); 
				System.out.println("Value : " + unmarshalledMcp.getValueList(a).values());
			}
			System.out.println("-------------------------------------------------------");
		}
		
		
		// Print la table entière pour vérifier que les Criterion et Alternative objects sont bien identifiés et uniques via l'ID
		System.out.println(mcp.getTableEval());
	}

}
