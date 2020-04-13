package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.FieldOperator;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldOperatorXml implements FieldOperator {

    private String value = "";

    @Override
    public String getInitialValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setInitialValue(String value) {
        this.value = value;
    }
}
