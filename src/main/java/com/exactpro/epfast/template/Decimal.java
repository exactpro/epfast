package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlElement;

public class Decimal extends FieldInstrContent {

    private Exponent exponent;

    private Mantissa mantissa;

    public Exponent getExponent() {
        return exponent;
    }

    @XmlElement(name = "exponent", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setExponent(Exponent exponent) {
        this.exponent = exponent;
    }

    public Mantissa getMantissa() {
        return mantissa;
    }

    @XmlElement(name = "mantissa", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setMantissa(Mantissa mantissa) {
        this.mantissa = mantissa;
    }
}
