package com.exactpro.epfast.template.xml.instructionfields.integerfields;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.Int32Field;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.helper.InstructionXml;

public class Int32Xml extends FieldInstrContent implements Int32Field, InstructionXml {

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
