package com.exactpro.epfast.template.xml.helper;

public class TemplateIdentity extends AbstractIdentity {

    public String parentTemplateNs;

    @Override
    public String getNamespace() {
        if (namespace != null) {
            return namespace;
        }
        if (parentTemplateNs != null) {
            return parentTemplateNs;
        }
        return "";
    }
}
