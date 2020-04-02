package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.LengthField;

import javax.xml.bind.annotation.XmlAttribute;

public class LengthXml extends FiledBaseXml implements LengthField, NamespaceProvider {

    private ApplicationIdentity fieldId = new ApplicationIdentity(null);

    private String localNamespace;

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
        if (localNamespace != null) {
            return localNamespace;
        }
        return fieldId.getNamespace();
    }

    @XmlAttribute(name = "namespace")
    public void setNamespace(String namespace) {
        this.localNamespace = namespace;
    }
}
