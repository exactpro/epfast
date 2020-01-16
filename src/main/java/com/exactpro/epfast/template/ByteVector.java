package com.exactpro.epfast.template;

import com.exactpro.epfast.template.additionalclasses.Length;

import javax.xml.bind.annotation.XmlElement;

public class ByteVector extends FieldInstrContent {

    private Length byteVectorLength;

    public Length getByteVectorLength() {
        return byteVectorLength;
    }

    @XmlElement(name = "length", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setByteVectorLength(Length byteVectorLength) {
        this.byteVectorLength = byteVectorLength;
    }
}
