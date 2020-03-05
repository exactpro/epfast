package com.exactpro.epfast.template.xml.instructionfields.integerfields;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.UInt32Field;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.InstructionXml;

public class UInt32Xml extends FieldInstrContent implements UInt32Field, InstructionXml {

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
