package com.exactpro.epfast.template.xml.instructionfields;

import javax.xml.bind.annotation.XmlAttribute;

public class TemplateRef {

    private String name;

    private String templateNs;

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateNs() {
        return templateNs;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }
}
