package com.exactpro.epfast.template;

import com.exactpro.epfast.template.namespacefields.TemplateNsName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template", namespace = Namespace.XML_NAMESPACE)
public class Template extends Instructions {

    private TemplateNsName templateNsName;

    private String ns;

    private String dictionary;

    private TypeRef typeRef;

    public TemplateNsName getTemplateNsName() {
        return templateNsName;
    }

    @XmlElement(name = "templateNsName", namespace = Namespace.XML_NAMESPACE)
    public void setTemplateNsName(TemplateNsName templateNsName) {
        this.templateNsName = templateNsName;
    }

    public String getNs() {
        return ns;
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = Namespace.XML_NAMESPACE)
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

}
