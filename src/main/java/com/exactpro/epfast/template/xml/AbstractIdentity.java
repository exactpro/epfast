package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Identity;

public abstract class AbstractIdentity implements Identity {

    private String name;

    private String auxiliaryId;

    private final NamespaceProvider nsProvider;

    protected AbstractIdentity(NamespaceProvider nsProvider) {
        this.nsProvider = nsProvider;
    }

    protected NamespaceProvider getNamespaceProvider() {
        return nsProvider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuxiliaryId() {
        return auxiliaryId;
    }
}
