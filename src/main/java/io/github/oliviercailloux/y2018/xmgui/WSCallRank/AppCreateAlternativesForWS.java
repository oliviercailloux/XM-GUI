package io.github.oliviercailloux.y2018.xmgui.WSCallRank;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;
import io.github.oliviercailloux.y2018.xmgui.file1.MCProblemMarshaller;
import io.github.oliviercailloux.y2018.xmgui.file2.AlternativesRankingMarshaller;

/*
 * This class is used to create clean Alternatives and their ranking 
 * in order to be used by the AltsCallRank class.
 */
public class AppCreateAlternativesForWS {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AppCreateAlternativesForWS.class);

	public static void main(String[] args) throws FileNotFoundException, JAXBException, IOException {
		
		String pathAlts="AlternativesForWS.xml";
		Path altsfilepath= Paths.get(pathAlts);
		
		String pathAltsRank="AlternativesRankingFile.xml";
		Path altsRankFilePath= Paths.get(pathAltsRank);
		
		Alternative alt1= new Alternative(1);
		Alternative alt2 = new Alternative(2);
		Alternative alt3 = new Alternative(3);
		MCProblem mcp = new MCProblem();
		mcp.addAlt(alt1);
		mcp.addAlt(alt2);
		mcp.addAlt(alt3);
		AlternativesRanking AltR1 = new AlternativesRanking(1,alt2);
		AltR1.putAltRank(2,alt1);
		AltR1.putAltRank(3,alt3);
	
		MCProblemMarshaller mcpMarshaller = new MCProblemMarshaller(mcp);
		try (final FileOutputStream fos = new FileOutputStream(pathAlts)) {
			mcpMarshaller.marshalAndWrite(fos);
			LOGGER.info("Marshalling invoked");
		}

		AlternativesRankingMarshaller AltRMarshaller = new AlternativesRankingMarshaller(AltR1);
		try (final FileOutputStream fos = new FileOutputStream(pathAltsRank)) {
			AltRMarshaller.writeAlternativeValueFromAlternativesRanking(fos);
			LOGGER.debug("Marshalling invoked");
		}
	
	}

}
