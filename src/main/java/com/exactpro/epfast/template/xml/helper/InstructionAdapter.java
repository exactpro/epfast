package com.exactpro.epfast.template.xml.helper;

import com.exactpro.epfast.template.Instruction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class InstructionAdapter extends XmlAdapter<InstructionXml, Instruction> {

    @Override
    public Instruction unmarshal(InstructionXml v) {
        return v.toInstruction();
    }

    @Override
    public InstructionXml marshal(Instruction v) {
        throw new UnsupportedOperationException("Marshalling of FAST XML schema is not supported");
    }
}
