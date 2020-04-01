package com.exactpro.epfast.template.xml.helper;

public class TemplateIdentity extends AbstractIdentity {

    public TemplateIdentity(NamespaceProvider nsProvider) {
        super(nsProvider);
    }

    @Override
    public String getNamespace() {
        if (getNamespaceProvider() != null) {
            return getNamespaceProvider().getTemplateNamespace();
        }
        return "";
    }
}
