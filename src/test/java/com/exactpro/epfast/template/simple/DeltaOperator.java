package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Dictionary;

public class DeltaOperator extends FieldOperator implements com.exactpro.epfast.template.DeltaOperator {

    private Reference dictionaryKey;

    private Dictionary dictionary;

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @Override
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
