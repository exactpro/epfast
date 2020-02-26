package com.exactpro.epfast.template.xml.namespacefields;

import com.exactpro.epfast.template.xml.helper.MainFields;

import javax.xml.bind.annotation.XmlAttribute;

public class NsName extends MainFields {

    private String ns;

    public String getNs() {
        return ns;
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.ns = ns;
    }

}