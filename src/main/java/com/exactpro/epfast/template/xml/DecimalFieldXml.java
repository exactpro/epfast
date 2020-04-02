package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.*;

import javax.xml.bind.annotation.XmlElement;

public class DecimalFieldXml extends FieldInstrContent implements InstructionXml {

    private FiledBaseXml exponent;

    private FiledBaseXml mantissa;

    public FiledBaseXml getExponent() {
        return exponent;
    }

    @XmlElement(name = "exponent", namespace = XML_NAMESPACE)
    public void setExponent(FiledBaseXml exponent) {
        this.exponent = exponent;
    }

    public FiledBaseXml getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa", namespace = XML_NAMESPACE)
    public void setMantissa(FiledBaseXml mantissa) {
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
