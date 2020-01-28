package com.exactpro.epfast.template;

import com.exactpro.epfast.template.namespacefields.NsName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class FieldInstrContent extends FieldOp {

    private NsName nsName;

    private PresenceAttr presenceAttr;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setNsName(NsName nsName) {
        this.nsName = nsName;
    }

    public PresenceAttr getPresenceAttr() {
        return presenceAttr;
    }

    @XmlAttribute(name = "presence")
    public void setPresenceAttr(PresenceAttr presenceAttr) {
        this.presenceAttr = presenceAttr;
    }

}
