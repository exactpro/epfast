package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;

public class OpContext extends InitialValueAttr {

    private String dictionary;

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }
}

