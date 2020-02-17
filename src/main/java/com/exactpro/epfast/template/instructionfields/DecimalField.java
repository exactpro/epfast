package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.FieldInstrContent;
import com.exactpro.epfast.template.FieldOp;
import com.exactpro.epfast.template.helper.Namespace;

import javax.xml.bind.annotation.XmlElement;

public class DecimalField extends FieldInstrContent {

    private FieldOp exponent;

    private FieldOp mantissa;

    public FieldOp getExponent() {
        return exponent;
    }

    @XmlElement(name = "exponent", namespace = Namespace.XML_NAMESPACE)
    public void setExponent(FieldOp exponent) {
        this.exponent = exponent;
    }

    public FieldOp getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa", namespace = Namespace.XML_NAMESPACE)
    public void setMantissa(FieldOp mantissa) {
        this.mantissa = mantissa;
    }
}
