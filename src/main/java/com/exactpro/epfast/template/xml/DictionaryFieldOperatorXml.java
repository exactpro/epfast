/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

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
