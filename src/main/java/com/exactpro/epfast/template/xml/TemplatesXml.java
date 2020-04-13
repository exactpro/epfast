package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.Templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "templates", namespace = NamespaceProvider.XML_NAMESPACE)
public class TemplatesXml implements Templates, NamespaceProvider {

    private NamespaceProvider parentNsProvider;

    private String applicationNs;

    private String templateNs;

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private List<Template> templates = new ArrayList<>();

    @Override
    public String getApplicationNamespace() {
        if (applicationNs != null) {
            return applicationNs;
        }
        if (parentNsProvider != null) {
            return parentNsProvider.getApplicationNamespace();
        }
        return Reference.DEFAULT_NAMESPACE;
    }

    @Override
    public String getTemplateNamespace() {
        if (templateNs != null) {
            return templateNs;
        }
        if (parentNsProvider != null) {
            return parentNsProvider.getTemplateNamespace();
        }
        return Reference.DEFAULT_NAMESPACE;
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        this.applicationNs = ns;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlElement(name = "dictionary", namespace = XML_NAMESPACE)
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public List<Template> getTemplates() {
        return templates;
    }

    @XmlElement(name = "template", type = TemplateXml.class, namespace = XML_NAMESPACE)
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}

