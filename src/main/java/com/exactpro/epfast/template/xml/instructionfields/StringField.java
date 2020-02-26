package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.xml.ByteVectorLength;
import com.exactpro.epfast.template.xml.helper.Charset;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class StringField extends FieldInstrContent {

    private Charset charset = Charset.ASCII;

    private ByteVectorLength byteVectorLength;

    public Charset getCharset() {
        return charset;
    }

    @XmlAttribute(name = "charset")
    public void setCharset(Charset charset) {
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
