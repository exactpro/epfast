package com.exactpro.epfast.template;

import com.exactpro.epfast.template.namespacefields.NsName;

import javax.xml.bind.annotation.XmlElement;

public class ByteVectorLength {

    private NsName nsName;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName")
    public void setNsName(NsName nsName) {
        this.nsName = nsName;
    }
}
