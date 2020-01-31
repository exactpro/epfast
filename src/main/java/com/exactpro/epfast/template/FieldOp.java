package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class FieldOp {
    final static String XML_NAMESPACE = "http://www.fixprotocol.org/ns/fast/td/1.1"; // TODO define it in single place and reuse

    private InitialValueAttr operator;

    public InitialValueAttr getOperator() {
        return operator;
    }

    @XmlElements({
        @XmlElement(name = "constant", type = InitialValueAttr.Constant.class, namespace = XML_NAMESPACE),
        @XmlElement(name = "default", type = InitialValueAttr.Default.class, namespace = XML_NAMESPACE),
        @XmlElement(name = "copy", type = InitialValueAttr.Copy.class, namespace = XML_NAMESPACE),
        @XmlElement(name = "increment", type = InitialValueAttr.Increment.class, namespace = XML_NAMESPACE),
        @XmlElement(name = "delta", type = InitialValueAttr.Delta.class, namespace = XML_NAMESPACE),
        @XmlElement(name = "tail", type = InitialValueAttr.Tail.class, namespace = XML_NAMESPACE)
    })
    public void setOperator(InitialValueAttr operator) {
        this.operator = operator;
    }

}
