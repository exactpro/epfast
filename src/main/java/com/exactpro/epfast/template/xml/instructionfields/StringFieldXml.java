package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.IdentityXml;
import com.exactpro.epfast.template.xml.InstructionXml;
import com.exactpro.epfast.template.xml.helper.Charset;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class StringFieldXml extends FieldInstrContent implements InstructionXml {

    private Charset charset = Charset.ASCII;

    private IdentityXml lengthFieldId;

    public Charset getCharset() {
        return charset;
    }

    @XmlAttribute(name = "charset")
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public IdentityXml getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlElement(name = "length", namespace = Namespace.XML_NAMESPACE)
    public void setLengthFieldId(IdentityXml lengthFieldId) {
        this.lengthFieldId = lengthFieldId;
    }

    @Override
    public Instruction toXmlInstruction() {
        return this;
    }
}

