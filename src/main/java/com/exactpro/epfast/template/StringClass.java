package com.exactpro.epfast.template;

import com.exactpro.epfast.template.additionalclasses.Length;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class StringClass extends FieldInstrContent {

    private CharSet charset;

    private Length byteVectorLength;

    public CharSet getCharset() {
        return charset;
    }

    @XmlAttribute(name = "charset")
    public void setCharset(CharSet charset) {
        this.charset = charset;
    }

    public Length getByteVectorLength() {
        return byteVectorLength;
    }

    @XmlElement(name = "length", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setByteVectorLength(Length byteVectorLength) {
        this.byteVectorLength = byteVectorLength;
    }
}

