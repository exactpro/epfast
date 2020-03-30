package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Template;
import com.exactpro.epfast.template.xml.helper.*;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template", namespace = Namespace.XML_NAMESPACE)
public class TemplateXml extends InstructionsXml implements Template, NamespaceProvider {

    private NamespaceProvider parentNsProvider;

    private TemplateIdentity templateId = new TemplateIdentity(parentNsProvider);

    private String applicationNs;

    private Dictionary dictionary;

    private ReferenceImpl typeRef;

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.templateId.setName(name);
    }

//    @XmlAttribute(name = "templateNs")
//    public void setTemplateNs(String templateNs) {
//        this.templateId.setNamespace(templateNs);
//    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.templateId.setAuxiliaryId(id);
    }

    @Override
    public String getTemplateNs() {
        return templateId.getNamespace();
    }

    @Override
    public String getNs() {
        return applicationNs;
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        this.applicationNs = ns;
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

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}
