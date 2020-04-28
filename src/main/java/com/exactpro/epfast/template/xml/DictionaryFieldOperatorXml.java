package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Reference;

import javax.xml.bind.annotation.XmlAttribute;

public class DictionaryFieldOperatorXml extends FieldOperatorXml {

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private String dictionaryKeyName = "";

    private String dictionaryKeyNs = Reference.DEFAULT_NAMESPACE;

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        this.dictionary = Dictionary.getDictionary(dictionary);
    }

    public ReferenceImpl getDictionaryKey() {
        return new ReferenceImpl(dictionaryKeyName, dictionaryKeyNs);
    }

    @XmlAttribute(name = "keyName")
    public void setDictionaryKeyName(String keyName) {
        this.dictionaryKeyName = keyName;
    }

    @XmlAttribute(name = "keyNs")
    public void setDictionaryKeyNs(String keyNs) {
        this.dictionaryKeyNs = keyNs;
    }
}
