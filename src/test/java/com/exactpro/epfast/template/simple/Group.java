package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Group extends FieldInstruction implements com.exactpro.epfast.template.Group {

    private Reference typeRef;

    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Reference getTypeRef() {
        return typeRef;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setTypeRef(Reference typeRef) {
        this.typeRef = typeRef;
    }
}
