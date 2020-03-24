package com.exactpro.epfast.template.xml.helper;

public class ApplicationIdentity extends AbstractIdentity {

    public String parentNs;

    @Override
    public String getNamespace() {
        if (namespace != null) {
            return namespace;
        }
        if (parentNs != null) {
            return parentNs;
        }
        return "";
    }
}
