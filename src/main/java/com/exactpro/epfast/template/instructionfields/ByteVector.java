package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.ByteVectorLength;
import com.exactpro.epfast.template.FieldInstrContent;
import com.exactpro.epfast.template.Namespace;

import javax.xml.bind.annotation.XmlElement;

public class ByteVector extends FieldInstrContent {

    private ByteVectorLength byteVectorLength;

    public ByteVectorLength getByteVectorLength() {
        return byteVectorLength;
    }

    @XmlElement(name = "length", namespace = Namespace.XML_NAMESPACE)
    public void setByteVectorLength(ByteVectorLength byteVectorLength) {
        this.byteVectorLength = byteVectorLength;
    }
}
