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
import com.exactpro.epfast.template.Templates;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class WrapperXml {

    public static List<? extends Template> wrapXmlInFASTTemplateList(InputStream xmlStream) throws JAXBException {
        Object template = JAXBContext.newInstance(TemplatesXml.class, TemplateXml.class)
            .createUnmarshaller().unmarshal(xmlStream);

        if (template instanceof Template) {
            return Collections.singletonList((Template) template);
        }
        return ((Templates) template).getTemplates();
    }
}
