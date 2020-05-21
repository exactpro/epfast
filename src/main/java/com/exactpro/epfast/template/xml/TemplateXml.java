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
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.Template;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template", namespace = NamespaceProvider.XML_NAMESPACE)
public class TemplateXml extends InstructionsXml implements Template, NamespaceProvider {

    private AbstractNamespaceProvider nsProvider = new AbstractNamespaceProvider();

    private TemplateIdentity templateId = new TemplateIdentity(this);

    private ReferenceImpl typeRef;

    private String typeRefName = "";

    private String typeRefNs;

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

    @Override
    public String getTemplateNamespace() {
        try {
            return nsProvider.getTemplateNamespace();
        } catch (NullPointerException exception) {
            return Reference.DEFAULT_NAMESPACE;
        }
    }

    @Override
    public String getApplicationNamespace() {
        try {
            return nsProvider.getApplicationNamespace();
        } catch (NullPointerException exception) {
            return Reference.DEFAULT_NAMESPACE;
        }
    }

    @Override
    public Dictionary getDictionary() {
        try {
            return nsProvider.getDictionary();
        } catch (NullPointerException exception) {
            return Dictionary.GLOBAL;
        }
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        nsProvider.setTemplateNs(templateNs);
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        nsProvider.setApplicationNs(ns);
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        nsProvider.setDictionaryName(dictionary);
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return typeRef;
    }

    @XmlAttribute(name = "typeRefName")
    public void setTypeRefName(String typeRefName) {
        this.typeRefName = typeRefName;
    }

    @XmlAttribute(name = "typeRefNs")
    public void setTypeRefNs(String typeRefNs) {
        this.typeRefNs = typeRefNs;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        nsProvider.afterUnmarshal(unmarshaller, parent);
        typeRef = new ReferenceImpl(typeRefName, typeRefNs);
        typeRef.afterUnmarshal(unmarshaller, this);
    }
}
