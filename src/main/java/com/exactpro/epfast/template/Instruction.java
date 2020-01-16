package com.exactpro.epfast.template;

import com.exactpro.epfast.template.additionalclasses.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

public class Instruction extends MainFields {

    final String namespace = "http://www.fixprotocol.org/ns/fast/td/1.1";

    private List elements;

    public List getElements() {
        return elements;
    }

    @XmlElements({
        @XmlElement(name = "templateRef", type = TemplateRef.class, namespace = namespace),
        @XmlElement(name = "uInt64", type = UInt64.class, namespace = namespace),
        @XmlElement(name = "uInt32", type = UInt32.class, namespace = namespace),
        @XmlElement(name = "int64", type = Int64.class, namespace = namespace),
        @XmlElement(name = "int32", type = Int32.class, namespace = namespace),
        @XmlElement(name = "decimal", type = DecimalField.class, namespace = namespace),
        @XmlElement(name = "string", type = StringClass.class, namespace = namespace),
        @XmlElement(name = "byteVector", type = ByteVector.class, namespace = namespace),
        @XmlElement(name = "sequence", type = Sequence.class, namespace = namespace),
        @XmlElement(name = "group", type = Group.class, namespace = namespace)
    })
    public void setElements(List elements) {
        this.elements = elements;
    }
}
