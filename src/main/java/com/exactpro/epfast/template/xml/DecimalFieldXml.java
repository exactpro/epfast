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

    @XmlElement(name = "exponent")
    public void setExponent(AbstractFieldXml exponent) {
        this.exponent = exponent;
    }

    public AbstractFieldXml getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa")
    public void setMantissa(AbstractFieldXml mantissa) {
        this.mantissa = mantissa;
    }

    private class CompoundDecimal implements CompoundDecimalField {
        @Override
        public FieldOperator getExponent() {
            AbstractFieldXml exponent = DecimalFieldXml.this.getExponent();
            return exponent == null ? null : exponent.getOperator();
        }

        @Override
        public FieldOperator getMantissa() {
            AbstractFieldXml mantissa = DecimalFieldXml.this.getMantissa();
            return mantissa == null ? null : mantissa.getOperator();
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

    private class SimpleDecimal implements SimpleDecimalField {
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
        if (mantissa == null && exponent == null) {
            return new SimpleDecimal();
        }
        return new CompoundDecimal();
    }
}
