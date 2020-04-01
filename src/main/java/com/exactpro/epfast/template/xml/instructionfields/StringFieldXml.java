package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.*;
import com.exactpro.epfast.template.xml.helper.ApplicationIdentity;
import com.exactpro.epfast.template.xml.helper.InstructionXml;
import com.exactpro.epfast.template.xml.helper.CharsetXml;
import com.exactpro.epfast.template.xml.FieldInstrContent;

import javax.xml.bind.annotation.XmlAttribute;

public class StringFieldXml extends FieldInstrContent implements InstructionXml {

    private CharsetXml charset = CharsetXml.ASCII;

    private ApplicationIdentity lengthFieldId = new ApplicationIdentity(null);

    public CharsetXml getCharset() {
        return charset;
    }

    @XmlAttribute(name = "charset")
    public void setCharset(CharsetXml charset) {
        this.charset = charset;
    }

    public ApplicationIdentity getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlAttribute(name = "lengthName")
    public void setName(String name) {
        this.lengthFieldId.setName(name);
    }

    @XmlAttribute(name = "lengthId")
    public void setId(String id) {
        this.lengthFieldId.setAuxiliaryId(id);
    }

    private class AsciiString implements AsciiStringField {
        @Override
        public FieldOperator getOperator() {
            return StringFieldXml.this.getOperator();
        }

        @Override
        public Identity getFieldId() {
            return StringFieldXml.this.getFieldId();
        }

        @Override
        public boolean isOptional() {
            return StringFieldXml.this.isOptional();
        }
    }

    private class UnicodeString implements UnicodeStringField {
        @Override
        public FieldOperator getOperator() {
            return StringFieldXml.this.getOperator();
        }

        @Override
        public Identity getLengthFieldId() {
            return StringFieldXml.this.getLengthFieldId();
        }

        @Override
        public Identity getFieldId() {
            return StringFieldXml.this.getFieldId();
        }

        @Override
        public boolean isOptional() {
            return StringFieldXml.this.isOptional();
        }
    }

    @Override
    public Instruction toInstruction() {
        switch (charset) {
            case ASCII:
                return new AsciiString();
            case UNICODE:
                return new UnicodeString();
            default:
                throw new IllegalArgumentException("Illegal Charset Value");
        }
    }
}
