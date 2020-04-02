package com.exactpro.epfast.template.xml;

public interface NamespaceProvider {

    String XML_NAMESPACE = "http://www.fixprotocol.org/ns/fast/td/1.1";

    String getTemplateNamespace();

    String getApplicationNamespace();

}
