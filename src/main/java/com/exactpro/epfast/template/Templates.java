package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "templates", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
public class Templates {

    private String ns;

    private String templateNs;

    private String dictionary;

    private List<Template> templates;

    public String getNs() {
        return ns;
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getTemplateNs() {
        return templateNs;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    @XmlElement(name = "template", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}

