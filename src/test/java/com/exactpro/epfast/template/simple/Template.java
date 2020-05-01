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

package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Template implements com.exactpro.epfast.template.Template {

    private Identity templateId = new Identity();

    private Reference typeRef = new Reference();

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @Override
    public Reference getTypeRef() {
        return typeRef;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setTemplateId(Identity templateId) {
        this.templateId = templateId;
    }

    public void setTypeRef(Reference typeRef) {
        this.typeRef = typeRef;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
