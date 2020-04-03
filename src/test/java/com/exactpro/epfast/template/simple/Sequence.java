package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends FieldInstruction implements com.exactpro.epfast.template.Sequence {
   
    private Reference typeRef = new Reference();
    
    private LengthField length;
    
    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Reference getTypeRef() {
        return typeRef;
    }

    @Override
    public LengthField getLength() {
        return length;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setTypeRef(Reference typeRef) {
        this.typeRef = typeRef;
    }

    public void setLength(LengthField length) {
        this.length = length;
    }
}
