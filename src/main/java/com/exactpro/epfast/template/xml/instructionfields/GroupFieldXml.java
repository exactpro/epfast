package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Group;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.xml.helper.*;
import com.exactpro.epfast.template.xml.InstructionsXml;
import com.exactpro.epfast.template.xml.ReferenceImpl;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class GroupFieldXml extends InstructionsXml implements Group, InstructionXml, NamespaceProvider {

    private NamespaceProvider parentNsProvider;

    private ApplicationIdentity fieldId = new ApplicationIdentity(parentNsProvider);

    private PresenceXml presence = PresenceXml.MANDATORY;

    private Dictionary dictionary;

    private ReferenceImpl typeRef;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @Override
    public String getTemplateNamespace() {
        return null;
    }

    @Override
    public String getApplicationNamespace() {
        return fieldId.getNamespace();
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "presence")
    public void setPresence(PresenceXml presence) {
        this.presence = presence;
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

    @Override
    public boolean isOptional() {
        return presence.equals(PresenceXml.OPTIONAL);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}
