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

//    @XmlAttribute(name = "namespace")
//    public void setNamespace(String templateNs) {
//        this.fieldId.setNamespace(templateNs);
//    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @Override
    public String getTemplateNs() {
        return null;
    }

    @Override
    public String getNs() {
        return fieldId.getNamespace();
    }
}
