package com.exactpro.epfast.template.xml.namespacefields;

import com.exactpro.epfast.template.xml.helper.MainFields;

import javax.xml.bind.annotation.XmlAttribute;

public class TemplateNsName extends MainFields {

    private String templateNs;

    public String getTemplateNs() {
        return templateNs;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }

}
