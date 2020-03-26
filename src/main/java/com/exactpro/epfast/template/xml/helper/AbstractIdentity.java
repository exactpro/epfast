package com.exactpro.epfast.template.xml.helper;

import com.exactpro.epfast.template.Identity;

public abstract class AbstractIdentity implements Identity {

    private String name;

    protected String namespace;

    private String auxiliaryId;

    private final NamespaceProvider nsProvider;

    protected AbstractIdentity(NamespaceProvider nsProvider) {
        this.nsProvider = nsProvider;
    }

    public NamespaceProvider getNsProvider() {
        return nsProvider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
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
