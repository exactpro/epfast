package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Group;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.helper.InstructionXml;
import com.exactpro.epfast.template.xml.InstructionsXml;
import com.exactpro.epfast.template.xml.helper.AfterUnmarshal;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.helper.NsXmlParent;
import com.exactpro.epfast.template.xml.helper.Presence;
import com.exactpro.epfast.template.xml.ReferenceImpl;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class GroupXml extends InstructionsXml implements Group, InstructionXml, NsXmlParent {

    private AfterUnmarshal.ApplicationIdentity fieldId = new AfterUnmarshal.ApplicationIdentity();

    private Presence presence = Presence.MANDATORY;

    private Dictionary dictionary;

    private ReferenceImpl typeRef;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @Override
    public String getNs() {
        return fieldId.getNs();
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.fieldId.setNs(ns);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "presence")
    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlElement(name = "dictionary", namespace = Namespace.XML_NAMESPACE)
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = Namespace.XML_NAMESPACE)
    public void setTypeRef(ReferenceImpl typeRef) {
        this.typeRef = typeRef;
    }

    @Override
    public boolean isOptional() {
        return presence.equals(Presence.OPTIONAL);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NsXmlParent) {
            fieldId.parentNs = ((NsXmlParent) parent).getNs();
        }
    }
}
