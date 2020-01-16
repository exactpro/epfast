package com.exactpro.epfast.template;

import com.exactpro.epfast.template.fieldop.*;

import javax.xml.bind.annotation.XmlElement;

public class FieldInstrContent extends MainFields {

    final String namespace = "http://www.fixprotocol.org/ns/fast/td/1.1";

    private Constant constant;

    private Copy copy;

    private Default aDefault;

    private Delta delta;

    private Increment increment;

    private Tail tail;

    public Constant getConstant() {
        return constant;
    }

    @XmlElement(name = "constant", namespace = namespace)
    public void setConstant(Constant constant) {
        this.constant = constant;
    }

    public Copy getCopy() {
        return copy;
    }

    @XmlElement(name = "copy", namespace = namespace)
    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public Default getaDefault() {
        return aDefault;
    }

    @XmlElement(name = "default", namespace = namespace)
    public void setaDefault(Default aDefault) {
        this.aDefault = aDefault;
    }

    public Delta getDelta() {
        return delta;
    }

    @XmlElement(name = "delta", namespace = namespace)
    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    public Increment getIncrement() {
        return increment;
    }

    @XmlElement(name = "increment", namespace = namespace)
    public void setIncrement(Increment increment) {
        this.increment = increment;
    }

    public Tail getTail() {
        return tail;
    }

    @XmlElement(name = "tail", namespace = namespace)
    public void setTail(Tail tail) {
        this.tail = tail;
    }
}
