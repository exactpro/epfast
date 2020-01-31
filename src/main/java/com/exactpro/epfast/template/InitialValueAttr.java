package com.exactpro.epfast.template;

import javax.xml.bind.annotation.XmlAttribute;

public class InitialValueAttr {

    private String value;

    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setValue(String value) {
        this.value = value;
    }

    public static class Constant extends InitialValueAttr {
    }

    public static class Default extends InitialValueAttr {
    }

    public static class Copy extends InitialValueAttr {
    }

    public static class Increment extends InitialValueAttr {
    }

    public static class Delta extends InitialValueAttr {
    }

    public static class Tail extends InitialValueAttr {
    }
}
