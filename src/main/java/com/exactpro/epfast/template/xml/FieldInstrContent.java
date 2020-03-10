package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.FieldInstruction;
import com.exactpro.epfast.template.xml.helper.Presence;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldInstrContent extends FieldOpXml implements FieldInstruction {

    private IdentityXml fieldId = new IdentityXml();

    private Presence presence = Presence.MANDATORY;

    @Override
    public IdentityXml getFieldId() {
        return fieldId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.fieldId.setNamespace(templateNs);
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
}
