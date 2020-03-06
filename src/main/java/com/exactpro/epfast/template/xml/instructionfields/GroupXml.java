package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Group;
import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.IdentityXml;
import com.exactpro.epfast.template.xml.InstructionXml;
import com.exactpro.epfast.template.xml.InstructionsXml;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.helper.Presence;
import com.exactpro.epfast.template.xml.ReferenceImpl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class GroupXml extends InstructionsXml implements Group, InstructionXml {

    private IdentityXml fieldId;

    private Presence presence = Presence.mandatory;

    private Dictionary dictionary;

    private ReferenceImpl typeRef;

    @Override
    public IdentityXml getFieldId() {
        return fieldId;
    }

    @XmlElement(name = "nsName", namespace = Namespace.XML_NAMESPACE)
    public void setFieldId(IdentityXml fieldId) {
        this.fieldId = fieldId;
    }

    public Presence getPresence() {
        return presence;
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
        return presence.equals(Presence.optional);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
