/*
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
 */

package com.exactpro.epfast.template.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class DictionaryFieldOperatorXml extends FieldOperatorXml {

    private ApplicationIdReference dictionaryKey = new ApplicationIdReference(this);

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        super.setDictionaryName(dictionary);
    }

    public ApplicationIdReference getDictionaryKey() {
        return dictionaryKey;
    }

    @XmlAttribute(name = "key")
    public void setDictionaryKeyName(String key) {
        dictionaryKey.setName(key);
    }

    @Override
    @XmlAttribute(name = "ns")
    public void setApplicationNamespace(String ns) {
        super.setApplicationNamespace(ns);
    }
}
