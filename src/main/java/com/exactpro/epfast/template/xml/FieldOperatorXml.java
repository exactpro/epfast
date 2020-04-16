package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.FieldOperator;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldOperatorXml implements FieldOperator {

    private String initialValue = "";

    @Override
    public String getInitialValue() {
        return initialValue;
    }

    @XmlAttribute(name = "value")
    public void setInitialValue(String value) {
        this.initialValue = value;
    }
}
