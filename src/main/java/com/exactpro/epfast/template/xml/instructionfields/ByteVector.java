package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.xml.ByteVectorLength;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlElement;

public class ByteVector extends FieldInstrContent {

    private ByteVectorLength byteVectorLength;

    public ByteVectorLength getByteVectorLength() {
        return byteVectorLength;
    }

    @XmlElement(name = "byteVectorLength", namespace = Namespace.XML_NAMESPACE)
    public void setByteVectorLength(ByteVectorLength byteVectorLength) {
        this.byteVectorLength = byteVectorLength;
    }
}
