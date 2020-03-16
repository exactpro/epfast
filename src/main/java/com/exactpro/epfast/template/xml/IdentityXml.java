package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Identity;

public class IdentityXml implements Identity {

    private String name;

    private String namespace;

    private String auxiliaryId;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getAuxiliaryId() {
        return auxiliaryId;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }
}
