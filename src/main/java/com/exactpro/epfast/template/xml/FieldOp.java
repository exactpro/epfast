package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.xml.helper.Namespace;
import com.exactpro.epfast.template.xml.operatorfields.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class FieldOp {

    private InitialValueAttr operator;

    public InitialValueAttr getOperator() {
        return operator;
    }

    @XmlElements({
        @XmlElement(name = "constant", type = Constant.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "default", type = Default.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "copy", type = Copy.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "increment", type = Increment.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "delta", type = Delta.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "tail", type = Tail.class, namespace = Namespace.XML_NAMESPACE)
    })
    public void setOperator(InitialValueAttr operator) {
        this.operator = operator;
    }

}
