package com.exactpro.epfast.template.xml.instructionfields.integerfields;

import com.exactpro.epfast.template.Instruction;
import com.exactpro.epfast.template.Int64Field;
import com.exactpro.epfast.template.xml.FieldInstrContent;
import com.exactpro.epfast.template.xml.InstructionXml;
import com.exactpro.epfast.template.xml.helper.NsXmlParent;

public class Int64Xml extends FieldInstrContent implements Int64Field, InstructionXml, NsXmlParent {

    @Override
    public Instruction toInstruction() {
        return this;
    }
}
