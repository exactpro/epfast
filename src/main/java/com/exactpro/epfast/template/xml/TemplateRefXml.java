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

public class TemplateRefXml extends DelegatingNamespaceProvider implements TemplateRef, InstructionXml {

    private TemplateIdReference templateRef = new TemplateIdReference(this);

    @Override
    public Reference getTemplateRef() {
        return templateRef;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        templateRef.setName(name);
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        templateRef.setNamespace(templateNs);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
