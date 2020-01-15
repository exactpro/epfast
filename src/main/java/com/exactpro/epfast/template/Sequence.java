package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Sequence extends Instruction {

    private String dictionary;

    private TypeRef typeRef;

    private Length length;

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public Length getLength() {
        return length;
    }

    @XmlElement(name = "length", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setLength(Length length) {
        this.length = length;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }
}

