package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.*;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.FieldOpXml;
import com.exactpro.epfast.template.xml.InstructionXml;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.helper.NsXmlParent;

import javax.xml.bind.annotation.XmlElement;

public class DecimalFieldXml extends FieldInstrContent implements InstructionXml, NsXmlParent {

    private FieldOpXml exponent;

    private FieldOpXml mantissa;

    public FieldOpXml getExponent() {
        return exponent;
    }

    @XmlElement(name = "exponent", namespace = Namespace.XML_NAMESPACE)
    public void setExponent(FieldOpXml exponent) {
        this.exponent = exponent;
    }

    public FieldOpXml getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa", namespace = Namespace.XML_NAMESPACE)
    public void setMantissa(FieldOpXml mantissa) {
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
