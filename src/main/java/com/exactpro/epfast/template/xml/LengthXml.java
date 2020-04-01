package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.LengthField;
import com.exactpro.epfast.template.xml.helper.ApplicationIdentity;
import com.exactpro.epfast.template.xml.helper.NamespaceProvider;

import javax.xml.bind.annotation.XmlAttribute;

public class LengthXml extends FiledBaseXml implements LengthField, NamespaceProvider {

    private ApplicationIdentity fieldId = new ApplicationIdentity(null);

    @Override
    public ApplicationIdentity getFieldId() {
        return fieldId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @Override
    public String getTemplateNamespace() {
        return null;
    }

    @Override
    public String getApplicationNamespace() {
        return fieldId.getNamespace();
    }
}
