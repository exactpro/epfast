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

import com.exactpro.epfast.template.FieldInstruction;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Instruction;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;

public class FieldInstrContent extends AbstractFieldXml implements FieldInstruction, NamespaceProvider {

    private NamespaceProvider parentNsProvider;

    private ApplicationIdentity fieldId = new ApplicationIdentity(this);

    private String localNamespace;

    private PresenceXml presence = PresenceXml.MANDATORY;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @Override
    public String getTemplateNamespace() {
        return null;
    }

    @Override
    public String getApplicationNamespace() {
        if (localNamespace != null) {
            return localNamespace;
        }
        return parentNsProvider.getApplicationNamespace();
    }

    @XmlAttribute(name = "namespace")
    public void setNamespace(String namespace) {
        this.localNamespace = namespace;
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
    public boolean isOptional() {
        return presence == PresenceXml.OPTIONAL;
    }

    public Instruction toInstruction() {
        return this;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}
