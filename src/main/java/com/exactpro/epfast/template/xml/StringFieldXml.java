/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.*;

import javax.xml.bind.annotation.XmlAttribute;

public class StringFieldXml extends FieldInstrContent implements InstructionXml {

    private CharsetXml charset = CharsetXml.ASCII;

    private ApplicationIdentity lengthFieldId = new ApplicationIdentity(this);

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
