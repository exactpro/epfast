package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.ByteVectorField;
import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.IdentityXml;
import com.exactpro.epfast.template.xml.helper.InstructionXml;

import javax.xml.bind.annotation.XmlAttribute;

public class ByteVectorXml extends FieldInstrContent implements ByteVectorField, InstructionXml {

    private IdentityXml lengthFieldId = new IdentityXml();

    @Override
    public IdentityXml getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlAttribute(name = "lengthName")
    public void setName(String name) {
        this.lengthFieldId.setName(name);
    }

    @XmlAttribute(name = "lengthNamespace")
    public void setNamespace(String templateNs) {
        this.lengthFieldId.setNamespace(templateNs);
    }

    @XmlAttribute(name = "lengthId")
    public void setId(String id) {
        this.lengthFieldId.setAuxiliaryId(id);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
