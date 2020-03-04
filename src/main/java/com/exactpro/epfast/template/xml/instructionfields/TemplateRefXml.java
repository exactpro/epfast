package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.TemplateRef;
import com.exactpro.epfast.template.xml.InstructionXml;
import com.exactpro.epfast.template.xml.ReferenceImpl;

import javax.xml.bind.annotation.XmlAttribute;

public class TemplateRefXml implements TemplateRef, InstructionXml {

    private String name;

    private String templateNs;

    @Override
    public Reference getTemplateRef() {
        return new ReferenceImpl(name, templateNs);
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }

    @Override
    public Instruction toXmlInstruction() {
        return this;
    }
}
