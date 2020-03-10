package com.exactpro.epfast.template.xml.helper;

import javax.xml.bind.annotation.XmlEnumValue;

public enum Presence {
    @XmlEnumValue("mandatory") MANDATORY,
    @XmlEnumValue("optional") OPTIONAL
}
