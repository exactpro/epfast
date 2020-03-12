package com.exactpro.epfast.template.xml.instructionfields.integerfields;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.UInt64Field;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.InstructionXml;
import com.exactpro.epfast.template.xml.helper.NsXmlParent;

public class UInt64Xml extends FieldInstrContent implements UInt64Field, InstructionXml, NsXmlParent {

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
