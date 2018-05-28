package io.github.oliviercailloux.y2018.xmgui.file2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;

public class File2Test {
	
	@Test
	public void test() throws FileNotFoundException, JAXBException, IOException {
		String path="AlternativesRankingFile.xml";
		Path filepath= Paths.get(path);
		
		Alternative alt1= new Alternative(1);
		Alternative alt2= new Alternative(2);
		Alternative alt3= new Alternative(3);
		Alternative alt4= new Alternative(4);
		Alternative alt5= new Alternative(5);
		Alternative alt6= new Alternative(6);
		AlternativesRanking AltR1 = new AlternativesRanking(1,alt1);
		AltR1.putAltRank(2,alt2);
		AltR1.putAltRank(2,alt3);
		AltR1.putAltRank(3,alt4);
		AltR1.putAltRank(4,alt5);
		AltR1.putAltRank(5,alt6);
		
		//ecriture de AlternativesRankingFile.xml
		AlternativesRankingMarshaller AltRMarshaller = new AlternativesRankingMarshaller(AltR1);
		try (final FileOutputStream fos = new FileOutputStream(path)) {
			AltRMarshaller.writeAlternativeValueFromAlternativesRanking(fos);
		}
	
		//lecture de AlternativesRankingFile.xml
		AlternativesRankingUnmarshaller AltRUnmarshaller = new AlternativesRankingUnmarshaller();
		AlternativesRanking AltR2;
		try (InputStream in = java.nio.file.Files.newInputStream(filepath)) {
			AltR2 = AltRUnmarshaller.readAlternativesRankingFromXml(in);
		}
	
		assertEquals(AltR1,AltR2);
	
	}
	
}