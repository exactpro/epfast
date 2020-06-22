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

package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Dictionary;

public abstract class OperatorWithDictionary extends FieldOperator {

    private Reference dictionaryKey = new Reference();

    private Dictionary dictionary = Dictionary.GLOBAL;

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
