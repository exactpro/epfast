package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.Templates;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class WrapperXml {

    public static List<? extends Template> wrap(InputStream xmlStream) throws JAXBException {
        Object template = JAXBContext.newInstance(TemplatesXml.class, TemplateXml.class)
            .createUnmarshaller().unmarshal(xmlStream);

        if (template instanceof Template) {
            return Collections.singletonList((Template) template);
        }
        return ((Templates) template).getTemplates();
    }
}
