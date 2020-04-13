package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Instruction;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class InstructionsXml {

    private List<Instruction> instructions = new ArrayList<>();

    public List<Instruction> getInstructions() {
        return instructions;
    }

    @XmlJavaTypeAdapter(InstructionAdapter.class)
    @XmlElements({
        @XmlElement(name = "templateRef", type = TemplateRefXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "int32", type = Int32FieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "uInt32", type = UInt32FieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "int64", type = Int64FieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "uInt64", type = UInt64FieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "decimal", type = DecimalFieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "string", type = StringFieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "byteVector", type = ByteVectorFieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "sequence", type = SequenceFieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "group", type = GroupFieldXml.class, namespace = NamespaceProvider.XML_NAMESPACE)
    })
    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }
}
