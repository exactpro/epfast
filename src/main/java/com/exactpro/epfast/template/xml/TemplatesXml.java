package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.Templates;
import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.helper.NamespaceProvider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "templates", namespace = Namespace.XML_NAMESPACE)
public class TemplatesXml implements Templates, NamespaceProvider {

    private String applicationNs;

    private String templateNsXml;

    private Dictionary dictionary;

    private List<Template> templates;

    @Override
    public String getNs() {
        return applicationNs;
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        this.applicationNs = ns;
    }

    @Override
    public String getTemplateNs() {
        return templateNsXml;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNsXml(String templateNs) {
        this.templateNsXml = templateNs;
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

