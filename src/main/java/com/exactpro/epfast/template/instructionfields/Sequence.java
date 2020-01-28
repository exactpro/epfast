package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.Instructions;
import com.exactpro.epfast.template.Length;
import com.exactpro.epfast.template.PresenceAttr;
import com.exactpro.epfast.template.TypeRef;
import com.exactpro.epfast.template.namespacefields.NsName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Sequence extends Instructions {

    private NsName nsName;

    private PresenceAttr presenceAttr;

    private String dictionary;

    private TypeRef typeRef;

    private Length length;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
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

    @XmlElement(name = "typeRef", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

    public Length getLength() {
        return length;
    }

    @XmlElement(name = "length", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setLength(Length length) {
        this.length = length;
    }

}

