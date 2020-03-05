package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.Templates;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "templates", namespace = Namespace.XML_NAMESPACE)
public class TemplatesXml implements Templates {

    private String ns;

    private String templateNs;

    private Dictionary dictionary;

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

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlElement(name = "dictionary", namespace = Namespace.XML_NAMESPACE)
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public List<Template> getTemplates() {
        return templates;
    }

    @XmlElement(name = "template", type = TemplateXml.class, namespace = Namespace.XML_NAMESPACE)
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}

