package io.github.oliviercailloux.y2018.xmgui.file2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;

public class App {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws FileNotFoundException, JAXBException, IOException {
		
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
		
		//ecriture de file2
		AlternativesRankingMarshaller AltRMarshaller = new AlternativesRankingMarshaller(AltR1);
		try (final FileOutputStream fos = new FileOutputStream(path)) {
			AltRMarshaller.writeAlternativeValueFromAlternativesRanking(fos);
			LOGGER.debug("Marshalling invoked");
		}

		//lecture de file2
		AlternativesRanking AltR2;
		AlternativesRankingUnmarshaller AltRUnmarshaller = new AlternativesRankingUnmarshaller();
		try (InputStream in = java.nio.file.Files.newInputStream(filepath)) {
			AltR2 = AltRUnmarshaller.readAlternativesRankingFromXml(in);
			LOGGER.debug("Unmarshalling invoked");
		}
		
		ImmutableSetMultimap<Integer, Alternative> map = AltR2.getAltSet();
		for (int Rank : map.keySet()) {
			System.out.println("Rang numéro :" + Rank);
			ImmutableSet<Alternative> allAlt = map.get(Rank);
			for (Alternative Alt : allAlt){
				System.out.println("Alternative :" + Alt.getId());
			}
		}
	}

}
