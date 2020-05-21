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

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.Templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "templates", namespace = NamespaceProvider.XML_NAMESPACE)
public class TemplatesXml extends AbstractNamespaceProvider implements Templates {

    private List<Template> templates = new ArrayList<>();

    @Override
    public String getTemplateNamespace() {
        return super.getTemplateNamespace();
    }

    @Override
    public String getApplicationNamespace() {
        return super.getApplicationNamespace();
    }

    @Override
    public Dictionary getDictionary() {
        return super.getDictionary();
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        super.setTemplateNs(templateNs);
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        super.setApplicationNs(ns);
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        super.setDictionaryName(dictionary);
    }

    @Override
    public List<Template> getTemplates() {
        return templates;
    }

    @XmlElement(name = "template", type = TemplateXml.class, namespace = XML_NAMESPACE)
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}

