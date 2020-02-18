package com.exactpro.epfast.template.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class TypeRef {

    private String name;

    private String ns;

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getNs() {
        return ns;
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.ns = ns;
    }
}
