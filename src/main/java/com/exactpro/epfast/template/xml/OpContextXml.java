package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlElement;

public class OpContextXml extends InitialValueXml {

    private Dictionary dictionary;

    private ReferenceImpl dictionaryKey;

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlElement(name = "dictionary", namespace = Namespace.XML_NAMESPACE)
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public ReferenceImpl getDictionaryKey() {
        return dictionaryKey;
    }

    @XmlElement(name = "nsKey", namespace = Namespace.XML_NAMESPACE)
    public void setDictionaryKey(ReferenceImpl nsKey) {
        this.dictionaryKey = nsKey;
    }
}

