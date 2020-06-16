/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Template;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

public class XmlTemplateUnmarshaller {

    private final Unmarshaller templateUnmarshaller;

    public XmlTemplateUnmarshaller() throws JAXBException {
        templateUnmarshaller = JAXBContext.newInstance(TemplatesXml.class, TemplateXml.class).createUnmarshaller();
    }

    public List<? extends Template> unmarshall(InputStream inputStream) throws JAXBException {
        return unmarshall(inputStream, templateUnmarshaller::unmarshal);
    }

    public List<? extends Template> unmarshall(Reader reader) throws JAXBException {
        return unmarshall(reader, templateUnmarshaller::unmarshal);
    }

    @FunctionalInterface
    private interface UnmarshallFunction<T> {
        Object apply(T t) throws JAXBException;
    }

    private <T> List<? extends Template> unmarshall(T t, UnmarshallFunction<T> unmarshaller) throws JAXBException {
        try {
            Object template = unmarshaller.apply(t);

            if (template instanceof TemplateXml) {
                return Collections.singletonList((Template) template);
            }
            return ((TemplatesXml) template).getTemplates();
        } catch (JAXBException ex) {
            throw new JAXBException("Unable to unmarshall FAST XML template", ex);
        }
    }
}
