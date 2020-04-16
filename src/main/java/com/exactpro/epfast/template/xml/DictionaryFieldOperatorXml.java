package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Reference;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class DictionaryFieldOperatorXml extends FieldOperatorXml {

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private ReferenceImpl dictionaryKey = new ReferenceImpl("", Reference.DEFAULT_NAMESPACE);

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        this.dictionary = Dictionary.getDictionary(dictionary);
    }

    public ReferenceImpl getDictionaryKey() {
        return dictionaryKey;
    }

    @XmlElement(name = "nsKey", namespace = NamespaceProvider.XML_NAMESPACE)
    public void setDictionaryKey(ReferenceImpl dictionaryKey) {
        this.dictionaryKey = dictionaryKey;
    }
}

