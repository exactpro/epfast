package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.xml.helper.ApplicationIdentity;
import com.exactpro.epfast.template.xml.helper.NamespaceProvider;

import javax.xml.bind.Unmarshaller;

public class ReferenceImpl implements Reference {

    private final String name;

    private final String applicationNs;

    private NamespaceProvider parentNsProvider;

    private ApplicationIdentity fieldId = new ApplicationIdentity(parentNsProvider);

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
        return fieldId.getNamespace();
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}
