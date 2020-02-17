package com.exactpro.epfast.template;

import com.exactpro.epfast.template.helper.Namespace;
import com.exactpro.epfast.template.instructionfields.*;
import com.exactpro.epfast.template.instructionfields.integerfields.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

public class Instructions {

    private List<Object> instructions;

    public List<Object> getInstructions() {
        return instructions;
    }

    @XmlElements({
        @XmlElement(name = "templateRef", type = TemplateRef.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "int32", type = Int32.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "uInt32", type = UInt32.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "int64", type = Int64.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "uInt64", type = UInt64.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "decimal", type = DecimalField.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "string", type = StringField.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "byteVector", type = ByteVector.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "sequence", type = Sequence.class, namespace = Namespace.XML_NAMESPACE),
        @XmlElement(name = "group", type = Group.class, namespace = Namespace.XML_NAMESPACE)
    })
    public void setInstructions(List<Object> instructions) {
        this.instructions = instructions;
    }

}
