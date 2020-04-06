package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Reference;

import javax.xml.bind.Unmarshaller;

public class ReferenceImpl implements Reference {

    private final String name;

    private final String applicationNs;

    private NamespaceProvider parentNsProvider;

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
        if (applicationNs != null) {
            return applicationNs;
        }
        return parentNsProvider.getApplicationNamespace();
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}
