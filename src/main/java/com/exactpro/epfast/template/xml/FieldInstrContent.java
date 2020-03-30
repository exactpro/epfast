package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.FieldInstruction;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.helper.ApplicationIdentity;
import com.exactpro.epfast.template.xml.helper.NamespaceProvider;
import com.exactpro.epfast.template.xml.helper.PresenceXml;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;

public class FieldInstrContent extends FiledBaseXml implements FieldInstruction {

    private NamespaceProvider parentNsProvider;

    private ApplicationIdentity fieldId = new ApplicationIdentity(parentNsProvider);

    private PresenceXml presence = PresenceXml.MANDATORY;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

//    @XmlAttribute(name = "ns")
//    public void setNs(String ns) {
//        this.fieldId.setNamespace(ns);
//    }

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
