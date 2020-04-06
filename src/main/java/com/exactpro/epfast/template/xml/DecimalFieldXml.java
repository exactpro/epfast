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

import javax.xml.bind.annotation.XmlElement;

public class DecimalFieldXml extends FieldInstrContent implements InstructionXml {

    private AbstractFieldXml exponent;

    private AbstractFieldXml mantissa;

    public AbstractFieldXml getExponent() {
        return exponent;
    }

    @XmlElement(name = "exponent", namespace = XML_NAMESPACE)
    public void setExponent(AbstractFieldXml exponent) {
        this.exponent = exponent;
    }

    public AbstractFieldXml getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa", namespace = XML_NAMESPACE)
    public void setMantissa(AbstractFieldXml mantissa) {
        this.mantissa = mantissa;
    }

    class CompoundDecimal implements CompoundDecimalField {
        @Override
        public FieldOperator getExponent() {
            return (FieldOperator) DecimalFieldXml.this.getExponent();
        }

        @Override
        public FieldOperator getMantissa() {
            return (FieldOperator) DecimalFieldXml.this.getMantissa();
        }

        @Override
        public Identity getFieldId() {
            return DecimalFieldXml.this.getFieldId();
        }

        @Override
        public boolean isOptional() {
            return DecimalFieldXml.this.isOptional();
        }
    }

    class SimpleDecimal implements SimpleDecimalField {
        @Override
        public FieldOperator getOperator() {
            return DecimalFieldXml.this.getOperator();
        }

        @Override
        public Identity getFieldId() {
            return DecimalFieldXml.this.getFieldId();
        }

        @Override
        public boolean isOptional() {
            return DecimalFieldXml.this.isOptional();
        }
    }

    @Override
    public Instruction toInstruction() {
        if (mantissa == null) {
            return new SimpleDecimal();
        }
        return new CompoundDecimal();
    }
}
