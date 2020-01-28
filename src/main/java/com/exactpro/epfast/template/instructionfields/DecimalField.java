package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.FieldInstrContent;
import com.exactpro.epfast.template.FieldOp;

import javax.xml.bind.annotation.XmlElement;

public class DecimalField extends FieldInstrContent {

    private FieldOp exponent;

    private FieldOp mantissa;

    public FieldOp getExponent() {
        return exponent;
    }

    @XmlElement(name = "exponent", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setExponent(FieldOp exponent) {
        this.exponent = exponent;
    }

    public FieldOp getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setMantissa(FieldOp mantissa) {
        this.mantissa = mantissa;
    }
}
