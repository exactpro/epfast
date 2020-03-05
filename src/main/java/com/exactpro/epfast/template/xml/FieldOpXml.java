package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.operatorfields.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class FieldOpXml {

    private InitialValueXml operator;

    public InitialValueXml getOperator() {
        return operator;
    }

    @XmlElements({
        @XmlElement(name = "constant", type = ConstantXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "default", type = DefaultXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "copy", type = CopyXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "increment", type = IncrementXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "delta", type = DeltaXml.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "tail", type = TailXml.class, namespace = Namespace.XML_NAMESPACE)
    })
    public void setOperator(InitialValueXml operator) {
        this.operator = operator;
    }
}
