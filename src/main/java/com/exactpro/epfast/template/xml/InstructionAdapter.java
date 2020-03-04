package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Instruction;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;
import java.util.stream.Collectors;

public class InstructionAdapter extends XmlAdapter<List<Instruction>, List<InstructionXml>> {

    @Override
    public List<InstructionXml> unmarshal(List<Instruction> v) {
        throw new UnsupportedOperationException("The method is not implemented!");
    }

    @Override
    public List<Instruction> marshal(List<InstructionXml> v) {
        return v.stream().map(InstructionXml::toXmlInstruction).collect(Collectors.toList());
    }
}
