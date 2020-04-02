package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Instruction;

public abstract class FieldInstructionWithOperator extends FieldInstruction implements Instruction {

    protected FieldOperator operator;

    public FieldOperator getOperator() {
        return operator;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
