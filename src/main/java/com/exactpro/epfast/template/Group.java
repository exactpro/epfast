package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Group extends Instruction {

    private Dictionary dictionary;

    private TypeRef typeRef;

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }
}