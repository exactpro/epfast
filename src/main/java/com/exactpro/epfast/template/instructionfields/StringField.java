package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.ByteVectorLength;
import com.exactpro.epfast.template.CharSet;
import com.exactpro.epfast.template.FieldInstrContent;
import com.exactpro.epfast.template.Namespace;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class StringField extends FieldInstrContent {

    private CharSet charset;

    private ByteVectorLength byteVectorLength;

    public CharSet getCharset() {
        return charset;
    }

    @XmlAttribute(name = "charset")
    public void setCharset(CharSet charset) {
        this.charset = charset;
    }

    public ByteVectorLength getByteVectorLength() {
        return byteVectorLength;
    }

    @XmlElement(name = "byteVectorLength", namespace = Namespace.XML_NAMESPACE)
    public void setByteVectorLength(ByteVectorLength byteVectorLength) {
        this.byteVectorLength = byteVectorLength;
    }
}

