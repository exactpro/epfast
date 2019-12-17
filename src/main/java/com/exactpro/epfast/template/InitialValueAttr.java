package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;

class InitialValueAttr {

    private String value;

    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setValue(String value) {
        this.value = value;
    }
}
