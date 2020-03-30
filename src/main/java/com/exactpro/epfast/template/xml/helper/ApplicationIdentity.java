package com.exactpro.epfast.template.xml.helper;

public class ApplicationIdentity extends AbstractIdentity {

    public ApplicationIdentity(NamespaceProvider nsProvider) {
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
