package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Reference;

public class ReferenceImpl implements Reference {

    private final String name;

    private final String applicationNs;

    public ReferenceImpl(String name, String ns) {
        this.name = name;
        this.applicationNs = ns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return applicationNs;
    }
}
