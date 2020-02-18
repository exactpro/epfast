package com.exactpro.epfast.template.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class InitialValueAttr {

    private String value;

    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setValue(String value) {
        this.value = value;
    }

}
