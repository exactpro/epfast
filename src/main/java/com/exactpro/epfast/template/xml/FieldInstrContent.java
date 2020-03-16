package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.FieldInstruction;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.xml.helper.AfterUnmarshal;
import com.exactpro.epfast.template.xml.helper.NsXmlParent;
import com.exactpro.epfast.template.xml.helper.Presence;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;

public class FieldInstrContent extends FieldOpXml implements FieldInstruction {

    private AfterUnmarshal.ApplicationIdentity fieldId = new AfterUnmarshal.ApplicationIdentity();

    private Presence presence = Presence.MANDATORY;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.fieldId.setNs(ns);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "presence")
    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    @Override
    public boolean isOptional() {
        return presence == Presence.OPTIONAL;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NsXmlParent) {
            fieldId.parentNs = ((NsXmlParent) parent).getNs();
        }
    }
}
