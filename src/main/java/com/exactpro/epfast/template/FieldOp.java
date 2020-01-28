package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;

public class FieldOp {

    final String namespace = "http://www.fixprotocol.org/ns/fast/td/1.1";

    private List<InitialValueAttr> operator = new ArrayList<>(Arrays.asList(new InitialValueAttr[6]));

    public List<InitialValueAttr> getOperator() {
        return operator;
    }

//    private InitialValueAttr constant;
//
//    private InitialValueAttr aDefault;
//
//    private OpContext copy;
//
//    private OpContext increment;
//
//    private OpContext delta;
//
//    private OpContext tail;

    public InitialValueAttr getConstant() {
        return operator.get(0);
    }

    @XmlElement(name = "constant", namespace = namespace)
    public void setConstant(InitialValueAttr constant) {
        operator.add(0, constant);
    }

    public InitialValueAttr getaDefault() {
        return operator.get(1);
    }

    @XmlElement(name = "default", namespace = namespace)
    public void setaDefault(InitialValueAttr aDefault) {
        operator.add(1, aDefault);
    }

    public OpContext getCopy() {
        return (OpContext) operator.get(2);
    }

    @XmlElement(name = "copy", namespace = namespace)
    public void setCopy(OpContext copy) {
        operator.add(2, copy);
    }

    public OpContext getIncrement() {
        return (OpContext) operator.get(3);
    }

    @XmlElement(name = "increment", namespace = namespace)
    public void setIncrement(OpContext increment) {
        operator.add(3, increment);
    }

    public OpContext getDelta() {
        return (OpContext) operator.get(4);
    }

    @XmlElement(name = "delta", namespace = namespace)
    public void setDelta(OpContext delta) {
        operator.add(4, delta);
    }

    public OpContext getTail() {
        return (OpContext) operator.get(5);
    }

    @XmlElement(name = "tail", namespace = namespace)
    public void setTail(OpContext tail) {
        operator.add(5, tail);
    }
}
