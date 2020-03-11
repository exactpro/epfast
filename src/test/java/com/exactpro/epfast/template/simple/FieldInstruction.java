package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Instruction;

public abstract class FieldInstruction implements com.exactpro.epfast.template.FieldInstruction, Instruction {
    
    private Identity fieldId = new Identity();

    private boolean optional;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    public void setFieldId(Identity fieldId) {
        this.fieldId = fieldId;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
