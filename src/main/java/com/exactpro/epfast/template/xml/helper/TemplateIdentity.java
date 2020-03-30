package com.exactpro.epfast.template.xml.helper;

public class TemplateIdentity extends AbstractIdentity {

    public TemplateIdentity(NamespaceProvider nsProvider) {
        super(nsProvider);
    }

    @Override
    public String getNamespace() {
//        if (namespace != null) {
//            return namespace;
//        }
        if (getNsProvider() != null) {
            return getNsProvider().getNs();
        }
        return "";
    }
}
