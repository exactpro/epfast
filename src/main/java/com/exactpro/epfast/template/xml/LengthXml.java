package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.LengthField;

import javax.xml.bind.annotation.XmlAttribute;

public class LengthXml extends FieldOpXml implements LengthField {

    private IdentityXml fieldId = new IdentityXml();

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
}
