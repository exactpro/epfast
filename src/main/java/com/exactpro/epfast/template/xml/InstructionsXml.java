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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;

public class InstructionsXml extends AbstractNamespaceProvider {

    private ArrayList<Instruction> instructions = new ArrayList<>();

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    @XmlJavaTypeAdapter(InstructionAdapter.class)
    @XmlElements({
        @XmlElement(name = "templateRef", type = TemplateRefXml.class),
        @XmlElement(name = "int32", type = Int32FieldXml.class),
        @XmlElement(name = "uInt32", type = UInt32FieldXml.class),
        @XmlElement(name = "int64", type = Int64FieldXml.class),
        @XmlElement(name = "uInt64", type = UInt64FieldXml.class),
        @XmlElement(name = "decimal", type = DecimalFieldXml.class),
        @XmlElement(name = "string", type = StringFieldXml.class),
        @XmlElement(name = "byteVector", type = ByteVectorFieldXml.class),
        @XmlElement(name = "sequence", type = SequenceFieldXml.class),
        @XmlElement(name = "group", type = GroupFieldXml.class)
    })
    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }
}
