package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.Template;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template", namespace = NamespaceProvider.XML_NAMESPACE)
public class TemplateXml extends InstructionsXml implements Template, NamespaceProvider {

    private NamespaceProvider parentNsProvider;

    private TemplateIdentity templateId = new TemplateIdentity(this);

    private String templateNs;

    private String applicationNs;

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private String typeRefName = "";

    private String typeRefNs = Reference.DEFAULT_NAMESPACE;

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.templateId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.templateId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
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

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        this.applicationNs = ns;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        this.dictionary = Dictionary.getDictionary(dictionary);
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return new ReferenceImpl(typeRefName, typeRefNs);
    }

    @XmlAttribute(name = "typeRefName")
    public void setTypeRefName(String typeRefName) {
        this.typeRefName = typeRefName;
    }

    @XmlAttribute(name = "typeRefNs")
    public void setTypeRefNs(String typeRefNs) {
        this.typeRefNs = typeRefNs;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}
