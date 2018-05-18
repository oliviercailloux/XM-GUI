package io.github.oliviercailloux.y2018.xmgui.file2;

import io.github.oliviercailloux.xmcda_2_2_1_jaxb.ObjectFactory;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.XMCDA;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2AlternativeValue;
import io.github.oliviercailloux.xmcda_2_2_1_jaxb.X2Value;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract2.AlternativesRanking;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

public class AlternativesRankingMarshaller {
	
	private AlternativesRanking AltR;

	public AlternativesRankingMarshaller(AlternativesRanking AltR) {
		Objects.requireNonNull(AltR);
		this.AltR = AltR;
	}
    
	public X2Value putX2Value(int Value){
		X2Value X2Value = new X2Value();
		X2Value.setInteger(Value);
		return X2Value;
	}

	/**
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * ecrit dans un fichier XML le ranking des alternatives
	 */
	public void writeAlternativeValueFromAlternativesRanking(FileOutputStream fos) throws JAXBException, FileNotFoundException, IOException {
		
		final JAXBContext jc = JAXBContext.newInstance(XMCDA.class);
		final Marshaller marshaller = jc.createMarshaller();
		final ObjectFactory f = new ObjectFactory();
		final XMCDA xmcda = f.createXMCDA();
		final List<JAXBElement<?>> xmcdaSubElements = xmcda.getProjectReferenceOrMethodMessagesOrMethodParameters();
		
		ImmutableSetMultimap<Integer, Alternative> map = AltR.getAltSet();

		for (int Rank : map.keySet()) {
			ImmutableSet<Alternative> allAlt = map.get(Rank);
			for (Alternative Alt : allAlt){
				X2AlternativeValue X2AltV = f.createX2AlternativeValue();
				X2AltV.setAlternativeID("" + Alt.getId());
				X2AltV.getValueOrValues().add(putX2Value(Rank));
				xmcdaSubElements.add(f.createXMCDAAlternativeValue(X2AltV));
			}
		}
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(xmcda, fos);

	}
}
