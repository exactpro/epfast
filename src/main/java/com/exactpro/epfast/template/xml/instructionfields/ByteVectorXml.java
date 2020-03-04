package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.ByteVectorField;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.IdentityXml;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlElement;

public class ByteVectorXml extends FieldInstrContent implements ByteVectorField {

    private IdentityXml lengthFieldId;

    @Override
    public IdentityXml getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlElement(name = "length", namespace = Namespace.XML_NAMESPACE)
    public void setLengthFieldId(IdentityXml lengthFieldId) {
        this.lengthFieldId = lengthFieldId;
    }

}
