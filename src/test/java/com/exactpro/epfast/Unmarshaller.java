package com.exactpro.epfast;

import com.exactpro.epfast.template.Templates;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

public class Unmarshaller {

    public static Templates unmarshal(String filePath) throws JAXBException {

        return (Templates) JAXBContext.newInstance(Templates.class).createUnmarshaller().unmarshal(new File(filePath));

    }

}
