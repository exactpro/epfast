package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;

class OpContext extends InitialValueAttr {

    private Dictionary dictionary;

    private String key;

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String getKey() {
        return key;
    }

    @XmlAttribute(name = "key")
    public void setKey(String key) {
        this.key = key;
    }
}

