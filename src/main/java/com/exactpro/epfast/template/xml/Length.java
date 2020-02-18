package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.namespacefields.NsName;

import javax.xml.bind.annotation.XmlElement;

public class Length extends FieldOp {

    private NsName nsName;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName", namespace = Namespace.XML_NAMESPACE)
    public void setNsName(NsName nsName) {
        this.nsName = nsName;
    }

}
