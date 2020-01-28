package com.exactpro.epfast.template.namespacefields;

import javax.xml.bind.annotation.XmlAttribute;

public class NsKey {

    private String key;

    private String ns;

    public String getKey() {
        return key;
    }

    @XmlAttribute(name = "key")
    public void setKey(String key) {
        this.key = key;
    }

    public String getNs() {
        return ns;
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.ns = ns;
    }

}
