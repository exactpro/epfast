package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Dictionary;

public abstract class OperatorWithDictionary extends FieldOperator {

    private Reference dictionaryKey;

    private Dictionary dictionary;

    public Dictionary getDictionary() {
        return dictionary;
    }

    public Reference getDictionaryKey() {
        return dictionaryKey;
    }

    public void setDictionaryKey(Reference dictionaryKey) {
        this.dictionaryKey = dictionaryKey;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
