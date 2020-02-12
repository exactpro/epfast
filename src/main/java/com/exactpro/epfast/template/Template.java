package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template", namespace = Namespace.XML_NAMESPACE)
public class Template extends Instructions {

    private Identity templateId = new Identity();

    private String ns;

    private String dictionary;

    private TypeRef typeRef;

    public Identity getTemplateId() {
        return templateId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.templateId.setName(name);
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateId.setNamespace(templateNs);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.templateId.setOptionalId(id);
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
