package com.exactpro.epfast;

import com.exactpro.epfast.template.Templates;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class Unmarshaller {

    public static Templates unmarshal(String filePath) throws JAXBException {

        return (Templates) JAXBContext.newInstance(Templates.class).createUnmarshaller().unmarshal(new File(filePath));

    }

    @Test
    void testUnmarshal() throws Exception {

        Marshaller marshaller = JAXBContext.newInstance(Templates.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(unmarshal("/home/exp.exactpro.com/ana.ksovreli/Documents/input.xml"), System.out);

    }

}
