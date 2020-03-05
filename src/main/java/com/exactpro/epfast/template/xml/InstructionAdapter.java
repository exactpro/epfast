package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Instruction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class InstructionAdapter extends XmlAdapter<Instruction, InstructionXml> {

    @Override
    public InstructionXml unmarshal(Instruction v) {
        throw new UnsupportedOperationException("The method is not implemented!");
    }

    @Override
    public Instruction marshal(InstructionXml v) {
        return v.toXmlInstruction();
    }
}
