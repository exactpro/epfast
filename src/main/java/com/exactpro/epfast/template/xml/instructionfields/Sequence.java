package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.xml.Instructions;
import com.exactpro.epfast.template.xml.Length;
import com.exactpro.epfast.template.xml.TypeRef;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.helper.PresenceAttr;
import com.exactpro.epfast.template.xml.namespacefields.NsName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Sequence extends Instructions {

    private NsName nsName;

    private PresenceAttr presenceAttr = PresenceAttr.MANDATORY;

    private String dictionary;

    private TypeRef typeRef;

    private Length length;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName", namespace = Namespace.XML_NAMESPACE)
    public void setNsName(NsName nsName) {
        this.nsName = nsName;
    }

    public PresenceAttr getPresenceAttr() {
        return presenceAttr;
    }

    @XmlAttribute(name = "presence")
    public void setPresenceAttr(PresenceAttr presenceAttr) {
        this.presenceAttr = presenceAttr;
    }

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = Namespace.XML_NAMESPACE)
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

    public Length getLength() {
        return length;
    }

    @XmlElement(name = "length", namespace = Namespace.XML_NAMESPACE)
    public void setLength(Length length) {
        this.length = length;
    }

}

