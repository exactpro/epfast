package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.ByteVectorField;

import javax.xml.bind.annotation.XmlAttribute;

public class ByteVectorFieldXml extends FieldInstrContent implements ByteVectorField, InstructionXml {

    private ApplicationIdentity lengthFieldId = new ApplicationIdentity(this);

    @Override
    public ApplicationIdentity getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlAttribute(name = "lengthName")
    public void setName(String name) {
        this.lengthFieldId.setName(name);
    }

    @XmlAttribute(name = "lengthId")
    public void setId(String id) {
        this.lengthFieldId.setAuxiliaryId(id);
    }
}
