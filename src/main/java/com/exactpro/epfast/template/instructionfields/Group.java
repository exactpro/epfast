package com.exactpro.epfast.template.instructionfields;

import com.exactpro.epfast.template.Instructions;
import com.exactpro.epfast.template.Namespace;
import com.exactpro.epfast.template.PresenceAttr;
import com.exactpro.epfast.template.TypeRef;
import com.exactpro.epfast.template.namespacefields.NsName;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Group extends Instructions {

    private NsName nsName;

    private PresenceAttr presenceAttr;

    private String dictionary;

    private TypeRef typeRef;

    public NsName getNsName() {
        return nsName;
    }

    @XmlElement(name = "nsName", namespace = Namespace.XML_NAMESPACE)
    public void setNsName(NsName nsName) {
        this.nsName = nsName;
    }

    public PresenceAttr getPresenceAttr() {
        return presenceAttr;
    }

    @XmlAttribute(name = "presence")
    public void setPresenceAttr(PresenceAttr presenceAttr) {
        this.presenceAttr = presenceAttr;
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
