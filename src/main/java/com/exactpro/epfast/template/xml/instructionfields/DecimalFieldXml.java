package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.SimpleDecimalField;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.FieldOpXml;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlElement;

public class DecimalFieldXml extends FieldInstrContent implements SimpleDecimalField {

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

}
