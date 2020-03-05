package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.instructionfields.*;
import com.exactpro.epfast.template.xml.instructionfields.integerfields.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;
import java.util.stream.Collectors;

public class InstructionsXml {

    private List<InstructionXml> instructions;

    public List<Instruction> getInstructions() {
        return instructions.stream().map(v -> v.toXmlInstruction()).collect(Collectors.toList());
    }

    public List<InstructionXml> getInstructionsXml() {
        return instructions;
    }

    @XmlElements({
        @XmlElement(name = "templateRef", type = TemplateRefXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "int32", type = Int32Xml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "uInt32", type = UInt32Xml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "int64", type = Int64Xml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "uInt64", type = UInt64Xml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "decimal", type = DecimalFieldXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "string", type = StringFieldXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "byteVector", type = ByteVectorXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "sequence", type = SequenceXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "group", type = GroupXml.class, namespace = Namespace.XML_NAMESPACE)
    })
    public void setInstructionsXml(List<InstructionXml> instructions) {
        this.instructions = instructions;
    }
}
