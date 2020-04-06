package com.exactpro.epfast.template.xml;

public class ApplicationIdentity extends AbstractIdentity {

    public ApplicationIdentity(NamespaceProvider nsProvider) {
        super(nsProvider);
    }

    @Override
    public String getNamespace() {
        if (getNamespaceProvider() != null) {
            return getNamespaceProvider().getApplicationNamespace();
        }
        return DEFAULT_NAMESPACE;
    }
}
