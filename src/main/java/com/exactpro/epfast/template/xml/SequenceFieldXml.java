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

import com.exactpro.epfast.template.*;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SequenceFieldXml extends InstructionsXml implements Sequence, InstructionXml, NamespaceProvider {

    private AbstractNamespaceProvider nsProvider = new AbstractNamespaceProvider();

    private ApplicationIdentity fieldId = new ApplicationIdentity(this);

    private PresenceXml presence = PresenceXml.MANDATORY;

    private ReferenceImpl typeRef = new ReferenceImpl(this);

    private LengthXml length;

    @Override
    public String getTemplateNamespace() {
        return nsProvider.getTemplateNamespace();
    }

    @Override
    public String getApplicationNamespace() {
        return nsProvider.getApplicationNamespace();
    }

    @Override
    public Dictionary getDictionary() {
        return nsProvider.getDictionary();
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
    public Identity getFieldId() {
        return fieldId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "presence")
    public void setPresence(PresenceXml presence) {
        this.presence = presence;
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return typeRef;
    }

    @XmlAttribute(name = "typeRefName")
    public void setTypeRefName(String name) {
        typeRef.setName(name);
    }

    @XmlAttribute(name = "typeRefNs")
    public void setTypeRefNs(String ns) {
        typeRef.setNamespace(ns);
    }

    @Override
    public LengthXml getLength() {
        return length;
    }

    @XmlElement(name = "length", namespace = XML_NAMESPACE)
    public void setLength(LengthXml length) {
        this.length = length;
    }

    @Override
    public boolean isOptional() {
        return presence.equals(PresenceXml.OPTIONAL);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        nsProvider.afterUnmarshal(unmarshaller, parent);
    }
}

