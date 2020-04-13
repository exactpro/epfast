package com.exactpro.epfast.template.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class AbstractFieldXml {

    private FieldOperatorXml operator = new FieldOperatorXml();

    public FieldOperatorXml getOperator() {
        return operator;
    }

    @XmlElements({
        @XmlElement(name = "constant", type = ConstantOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "default", type = DefaultOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "copy", type = CopyOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "increment", type = IncrementOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "delta", type = DeltaOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "tail", type = TailOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE)
    })
    public void setOperator(FieldOperatorXml operator) {
        this.operator = operator;
    }
}
