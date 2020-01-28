package com.exactpro.epfast.template;

import com.exactpro.epfast.template.namespacefields.NsKey;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class OpContext extends InitialValueAttr {

    private String dictionary;

    private NsKey nsKey;

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public NsKey getNsKey() {
        return nsKey;
    }

    @XmlElement(name = "nsKey", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setNsKey(NsKey nsKey) {
        this.nsKey = nsKey;
    }

}

