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

import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template")
public class TemplateXml extends InstructionsXml implements Template, NamespaceProvider {

    private TemplateIdentity templateId = new TemplateIdentity(this);

    private TypeRefXml typeRef;

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.templateId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.templateId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNamespace(String templateNs) {
        super.setTemplateNamespace(templateNs);
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNamespace(String ns) {
        super.setApplicationNamespace(ns);
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        super.setDictionaryName(dictionary);
    }

    @Override
    public TypeRefXml getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = XML_NAMESPACE)
    public void setTypeRef(TypeRefXml typeRef) {
        this.typeRef = typeRef;
    }
}
