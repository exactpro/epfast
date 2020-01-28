package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.ByteVectorLength;
import com.exactpro.epfast.template.FieldInstrContent;

import javax.xml.bind.annotation.XmlElement;

public class ByteVector extends FieldInstrContent {

    private ByteVectorLength byteVectorLength;

    public ByteVectorLength getByteVectorLength() {
        return byteVectorLength;
    }

    @XmlElement(name = "length", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setByteVectorLength(ByteVectorLength byteVectorLength) {
        this.byteVectorLength = byteVectorLength;
    }
}
