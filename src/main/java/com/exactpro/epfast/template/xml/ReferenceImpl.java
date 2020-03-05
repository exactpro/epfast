package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Reference;

public class ReferenceImpl implements Reference {

    private final String name;

    private final String namespace;

    public ReferenceImpl(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }
}
