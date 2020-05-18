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

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.TemplateRef;

import javax.xml.bind.annotation.XmlAttribute;

public class TemplateRefXml implements TemplateRef, InstructionXml {

    private String name = "";

    private String templateNs = Reference.DEFAULT_NAMESPACE;

    @Override
    public Reference getTemplateRef() {
        return new ReferenceImpl(name, templateNs);
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
