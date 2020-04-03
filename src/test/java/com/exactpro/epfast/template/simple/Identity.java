package com.exactpro.epfast.template.simple;

public class Identity extends Reference implements com.exactpro.epfast.template.Identity {

    private String auxiliaryId;

    public Identity() {
    }

    public Identity(String name) {
        super(name);
    }

    public Identity(String name, String namespace) {
        super(name, namespace);
    }

    public Identity(String name, String namespace, String auxiliaryId) {
        super(name, namespace);
        this.auxiliaryId = auxiliaryId;
    }

    @Override
    public String getAuxiliaryId() {
        return auxiliaryId;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }
}
