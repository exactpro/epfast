package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.*;

import javax.xml.bind.annotation.XmlElement;

public class DecimalFieldXml extends FieldInstrContent implements InstructionXml {

    private AbstractFieldXml exponent = new AbstractFieldXml();

    private AbstractFieldXml mantissa = new AbstractFieldXml();

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
            return DecimalFieldXml.this.getExponent().getOperator();
        }

        @Override
        public FieldOperator getMantissa() {
            return DecimalFieldXml.this.getMantissa().getOperator();
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
