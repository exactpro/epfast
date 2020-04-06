package com.exactpro.epfast.template.xml;

public class TemplateIdentity extends AbstractIdentity {

    public TemplateIdentity(NamespaceProvider nsProvider) {
        super(nsProvider);
    }

    @Override
    public String getNamespace() {
        if (getNamespaceProvider() != null) {
            return getNamespaceProvider().getTemplateNamespace();
        }
        return DEFAULT_NAMESPACE;
    }
}
