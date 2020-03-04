package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Sequence;
import com.exactpro.epfast.template.xml.IdentityXml;
import com.exactpro.epfast.template.xml.InstructionsXml;
import com.exactpro.epfast.template.xml.LengthXml;
import com.exactpro.epfast.template.xml.ReferenceImpl;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.helper.Presence;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SequenceXml extends InstructionsXml implements Sequence {

    private IdentityXml fieldId;

    private Presence presence = Presence.MANDATORY;

    private Dictionary dictionary;

    private ReferenceImpl typeRef;

    private LengthXml length;

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
    public LengthXml getLength() {
        return length;
    }

    @XmlElement(name = "length", namespace = Namespace.XML_NAMESPACE)
    public void setLength(LengthXml length) {
        this.length = length;
    }

    @Override
    public boolean isOptional() {
        return presence.equals(Presence.OPTIONAL);
    }

}

