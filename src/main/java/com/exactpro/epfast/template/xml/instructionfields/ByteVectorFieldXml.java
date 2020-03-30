package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.ByteVectorField;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.helper.ApplicationIdentity;
import com.exactpro.epfast.template.xml.helper.InstructionXml;

import javax.xml.bind.annotation.XmlAttribute;

public class ByteVectorFieldXml extends FieldInstrContent implements ByteVectorField, InstructionXml {

    private ApplicationIdentity lengthFieldId = new ApplicationIdentity(null);

    @Override
    public ApplicationIdentity getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlAttribute(name = "lengthName")
    public void setName(String name) {
        this.lengthFieldId.setName(name);
    }

//    @XmlAttribute(name = "lengthNamespace")
//    public void setNamespace(String templateNs) {
//        this.lengthFieldId.setNamespace(templateNs);
//    }

    @XmlAttribute(name = "lengthId")
    public void setId(String id) {
        this.lengthFieldId.setAuxiliaryId(id);
    }
}
