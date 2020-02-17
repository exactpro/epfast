package com.exactpro.epfast.template;

import com.exactpro.epfast.template.Helper.Namespace;
import com.exactpro.epfast.template.namespacefields.NsName;

import javax.xml.bind.annotation.XmlElement;

public class ByteVectorLength {

    private NsName nsName;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName", namespace = Namespace.XML_NAMESPACE)
    public void setNsName(NsName nsName) {
        this.nsName = nsName;
    }
}
