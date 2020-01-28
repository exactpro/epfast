package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;

public class Field {

    private Identity identity = new Identity();

    @XmlAttribute
    void setName(String name) {
        identity.name = name;
    }

}
