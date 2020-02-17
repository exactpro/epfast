package com.exactpro.epfast.template.Helper;

import javax.xml.bind.annotation.XmlAttribute;

public class MainFields {

    private String name;

    private String id;

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

}
