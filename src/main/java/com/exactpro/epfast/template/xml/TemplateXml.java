package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.xml.helper.Namespace;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template", namespace = Namespace.XML_NAMESPACE)
public class TemplateXml extends InstructionsXml implements Template {

    private IdentityXml templateId = new IdentityXml();

    private String ns;

    private Dictionary dictionary;

    private ReferenceImpl typeRef;

    @Override
    public IdentityXml getTemplateId() {
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
        this.templateId.setAuxiliaryId(id);
    }

    public String getNs() {
        return ns;
    }

    @XmlAttribute(name = "ns")
    public void setNs(String ns) {
        this.ns = ns;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlElement(name = "dictionary", namespace = Namespace.XML_NAMESPACE)
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = Namespace.XML_NAMESPACE)
    public void setTypeRef(ReferenceImpl typeRef) {
        this.typeRef = typeRef;
    }
}
