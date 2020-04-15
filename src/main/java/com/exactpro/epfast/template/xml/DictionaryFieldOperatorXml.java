package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Reference;

import javax.xml.bind.annotation.XmlElement;

public class DictionaryFieldOperatorXml extends FieldOperatorXml {

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private ReferenceImpl dictionaryKey = new ReferenceImpl("", Reference.DEFAULT_NAMESPACE);

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlElement(name = "dictionary", namespace = Reference.DEFAULT_NAMESPACE)
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public ReferenceImpl getDictionaryKey() {
        return dictionaryKey;
    }

    @XmlElement(name = "nsKey", namespace = NamespaceProvider.XML_NAMESPACE)
    public void setDictionaryKey(ReferenceImpl dictionaryKey) {
        this.dictionaryKey = dictionaryKey;
    }
}

